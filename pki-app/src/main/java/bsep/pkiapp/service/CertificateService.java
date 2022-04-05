package bsep.pkiapp.service;

import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.keystores.KeyStoreWriter;
import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.model.CertificateType;
import bsep.pkiapp.repository.CertificateChainRepository;
import bsep.pkiapp.utils.X500NameGenerator;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

@Service
public class CertificateService {

    @Autowired
    private X500NameGenerator x500NameGenerator;

    @Autowired
    private ExtensionService extensionService;
	private CertificateChainRepository certificateChainRepository;

    public void createCertificate(NewCertificateDto dto) {
        X500Name subject = x500NameGenerator.generateX500Name(dto);
        if (dto.certificateType.equals("ROOT")) {
            generateCertificate(dto, subject, subject);
        }
        else {
            //TODO: get issuer from KeyStorage, check if issuer is of role: ROLE_CA
            X500Name issuer = null;
            generateCertificate(dto, issuer, subject);
        }
    }

    public void generateCertificate(NewCertificateDto dto, X500Name issuer, X500Name subject) {
        try {
            //TODO: validation checks, keyStorage, DataBase storage, extensions and purposes
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            KeyPair keyPair = generateKeyPair();

            ContentSigner contentSigner;
            if (dto.certificateType.equals("ROOT")) {
                contentSigner = builder.build(keyPair.getPrivate());
            }
            else {
                contentSigner = null; //TODO: private key of issuer
            }

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuer,
                    generateSerialNumber(),
                    new Date(),
                    dto.getValidityEndDate(),
                    subject,
                    keyPair.getPublic());

            dto.extensionSettingsDto.setPublicKey(keyPair.getPublic());
            dto.extensionSettingsDto.setSubjectName(subject);

            certGen = extensionService.addExtensions(certGen, dto.getExtensionSettingsDto(), dto.getCertificateType());

            X509CertificateHolder certHolder = certGen.build(contentSigner);

            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            X509Certificate certificate = certConverter.getCertificate(certHolder);
            if(dto.certificateType.equals("ROOT")) {
            	Date startDate = new Date();
                CertificateChain chain = new CertificateChain(0L, dto.organizationName, CertificateType.ROOT,null,startDate,dto.validityEndDate, true);
                writeRootCertificateToKeyStore(chain.getSerialNumber().toString(), keyPair.getPrivate(), certificate, chain.getSerialNumber().toString());
            }else {
            	Date startDate = new Date();
                CertificateChain chain = new CertificateChain(0L, dto.organizationName, CertificateType.ROOT,null,startDate,dto.validityEndDate, true);
                String rootSerialNumber = findRootSerialNumber();
                writeCertificateToHierarchyKeyStore(chain.getSerialNumber().toString(), rootSerialNumber, keyPair.getPrivate(), certificate, chain.getSerialNumber().toString());
            }
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException
                | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    public BigInteger generateSerialNumber() {
        byte[] bytes = new byte[8];
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA1PRNG was not a known algorithm", e);
        }
        random.setSeed(new Date().getTime());
        random.nextBytes(bytes);
        return new BigInteger(bytes).abs();
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void writeRootCertificateToKeyStore(String serialNumber, PrivateKey privateKey, Certificate certificate, String password) {
		KeyStoreWriter ksw = new KeyStoreWriter();
		ksw.loadKeyStore(null, password.toCharArray());
		ksw.write(serialNumber, privateKey, password.toCharArray(), certificate);
		ksw.saveKeyStore(".\\files\\root" + serialNumber + ".jks", password.toCharArray());
		
		KeyStoreWriter kswHierarchy = new KeyStoreWriter();
		kswHierarchy.loadKeyStore(null, password.toCharArray());
		kswHierarchy.saveKeyStore(".\\files\\hierarchy" + serialNumber + ".jks", password.toCharArray());
	}
    
    private void writeCertificateToHierarchyKeyStore(String serialNumber, String rootSerialNumber, PrivateKey privateKey, Certificate certificate, String password) {
		KeyStoreWriter ksw = new KeyStoreWriter();
		ksw.loadKeyStore(".\\files\\hierarchy" + rootSerialNumber + ".jks", password.toCharArray());
		ksw.write(serialNumber, privateKey, password.toCharArray(), certificate);
		ksw.saveKeyStore(".\\files\\hierarchy" + rootSerialNumber + ".jks", password.toCharArray());
	}
    
    private String findRootSerialNumber() {
    	List<CertificateChain> certificateChain = certificateChainRepository.findAll();
    	CertificateChain certificate = new CertificateChain();
    	while(!certificate.getCertificateType().equals(CertificateType.ROOT)) {
    		certificate = certificateChainRepository.getCertificateChainBySignerSerialNumber(certificate.getSignerSerialNumber());
    	}
    	return certificate.getSerialNumber().toString();
	}
}
