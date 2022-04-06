package bsep.pkiapp.utils;

import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.keystores.KeyStoreReader;
import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.model.CertificateType;
import bsep.pkiapp.model.User;
import bsep.pkiapp.repository.CertificateChainRepository;
import bsep.pkiapp.service.KeyStoreService;
import bsep.pkiapp.service.UserService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class X500NameGenerator {

    private X500NameBuilder nameBuilder;

    @Autowired
    private UserService userService;
    
    @Autowired
   	private CertificateChainRepository certificateChainRepository;

    public X500Name generateX500Name(NewCertificateDto newCertificateDto) {
        User subject = userService.getById(newCertificateDto.idSubject);
        X500Name x500NameIssuer = null;
        if (newCertificateDto.issuerSerialNumber != null) {
            //TODO: get certificate from KeyStorage
        	if(isIssuerRootCertificate(newCertificateDto.issuerSerialNumber)) {
        		KeyStoreReader keyStore = new KeyStoreReader();
                x500NameIssuer = keyStore.readIssuerNameFromStore(".\\files\\root" + newCertificateDto.issuerSerialNumber + ".jks", newCertificateDto.issuerSerialNumber, newCertificateDto.issuerSerialNumber.toCharArray(), newCertificateDto.issuerSerialNumber.toCharArray());
        	} else {
        		KeyStoreReader keyStore = new KeyStoreReader();
        		String rootSerialNumber = findRootSerialNumberByIssuerSerialNumber(newCertificateDto.issuerSerialNumber);
                x500NameIssuer = keyStore.readIssuerNameFromStore(".\\files\\hierarchy" + rootSerialNumber + ".jks", newCertificateDto.issuerSerialNumber, rootSerialNumber.toCharArray(), newCertificateDto.issuerSerialNumber.toCharArray());
        	}
        }
        X500Name x500Name;
        if (newCertificateDto.certificateType.equals("ROOT"))
            x500Name = generateX500NameForRoot(subject, newCertificateDto);
        else if (newCertificateDto.certificateType.equals("INTERMEDIATE"))
            x500Name = generateX500NameForIntermediate(subject, newCertificateDto, x500NameIssuer);
        else
            x500Name = generateX500NameForEndEntity(subject, newCertificateDto, x500NameIssuer);

        return x500Name;
    }
    
    private String findRootSerialNumberByIssuerSerialNumber(String issuerSerialNumber) {
    	CertificateChain certificate = certificateChainRepository.getById(Long.decode(issuerSerialNumber));
    	while(!(CertificateType.ROOT).equals(certificate.getCertificateType())) {
    		certificate = certificateChainRepository.getCertificateChainBySignerSerialNumber(certificate.getSignerSerialNumber());
    	}
    	return certificate.getSerialNumber().toString();
	}

	private boolean isIssuerRootCertificate(String issuerSerialNumber) {
		// TODO Auto-generated method stub
    	CertificateChain certificate = certificateChainRepository.getById(Long.decode(issuerSerialNumber));
		return (CertificateType.ROOT).equals(certificate.getCertificateType());
	}

    private X500Name generateX500NameForRoot(User subject, NewCertificateDto newCertificateDto) {
        nameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        nameBuilder.addRDN(BCStyle.CN, newCertificateDto.organizationName);
        nameBuilder.addRDN(BCStyle.O, newCertificateDto.organizationName);
        nameBuilder.addRDN(BCStyle.C, newCertificateDto.country);
        nameBuilder.addRDN(BCStyle.E, subject.getEmail());
        nameBuilder.addRDN(BCStyle.UID, subject.getId().toString());
        return nameBuilder.build();
    }

    private X500Name generateX500NameForIntermediate(User subject, NewCertificateDto newCertificateDto, X500Name x500NameIssuer) {
        nameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        nameBuilder.addRDN(BCStyle.CN, newCertificateDto.organizationUnit);
        nameBuilder.addRDN(BCStyle.O, IETFUtils.valueToString((x500NameIssuer.getRDNs(BCStyle.O)[0]).getFirst().getValue()));
        nameBuilder.addRDN(BCStyle.OU, newCertificateDto.organizationUnit);
        nameBuilder.addRDN(BCStyle.C, IETFUtils.valueToString((x500NameIssuer.getRDNs(BCStyle.C)[0]).getFirst().getValue()));
        nameBuilder.addRDN(BCStyle.E, subject.getEmail());
        nameBuilder.addRDN(BCStyle.UID, subject.getId().toString());
        return nameBuilder.build();
    }

    private X500Name generateX500NameForEndEntity(User subject, NewCertificateDto newCertificateDto, X500Name x500NameIssuer) {
        nameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        nameBuilder.addRDN(BCStyle.CN, subject.getName() + " " + subject.getSurname());
        nameBuilder.addRDN(BCStyle.GIVENNAME, subject.getName());
        nameBuilder.addRDN(BCStyle.SURNAME, subject.getSurname());
        nameBuilder.addRDN(BCStyle.O, IETFUtils.valueToString((x500NameIssuer.getRDNs(BCStyle.O)[0]).getFirst().getValue()));
        nameBuilder.addRDN(BCStyle.OU,IETFUtils.valueToString((x500NameIssuer.getRDNs(BCStyle.OU)[0]).getFirst().getValue()));
        nameBuilder.addRDN(BCStyle.C, IETFUtils.valueToString((x500NameIssuer.getRDNs(BCStyle.C)[0]).getFirst().getValue()));
        nameBuilder.addRDN(BCStyle.E, subject.getEmail());
        nameBuilder.addRDN(BCStyle.UID, subject.getId().toString());
        return nameBuilder.build();
    }


}
