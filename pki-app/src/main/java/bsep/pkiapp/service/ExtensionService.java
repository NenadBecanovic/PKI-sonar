package bsep.pkiapp.service;

import bsep.pkiapp.dto.ExtensionSettingsDto;
import bsep.pkiapp.model.*;
import bsep.pkiapp.repository.ExtensionRepository;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExtensionService {

    @Autowired
    private ExtensionRepository extensionRepository;

    @Autowired
    private KeyUsageService keyUsageService;

    @Autowired
    private ExtendedKeyUsageService extendedKeyUsageService;

    public X509v3CertificateBuilder addExtensions(X509v3CertificateBuilder certGen, ExtensionSettingsDto dto, String certificateType, PublicKey issuerPublicKey) throws IOException, NoSuchAlgorithmException {
        log.debug("AENC with TP: {}", certificateType);
        for (Integer id : dto.getExtensionsIds()) {
            CertificateExtension extension = extensionRepository.getById(id);

            if (extension.getExtensionType().equals(ExtensionType.BASIC_CONSTRAINTS)) {
                certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(!certificateType.equals("END_ENTITY")));
            } else if (extension.getExtensionType().equals(ExtensionType.SUBJECT_KEY_ID)) {
                certGen.addExtension(Extension.subjectKeyIdentifier, true, new JcaX509ExtensionUtils().createSubjectKeyIdentifier(dto.getPublicKey()));
            } else if (extension.getExtensionType().equals(ExtensionType.AUTHORITY_KEY_ID)) {
                certGen.addExtension(Extension.authorityKeyIdentifier, true, new JcaX509ExtensionUtils().createAuthorityKeyIdentifier(issuerPublicKey));
            } else if (extension.getExtensionType().equals(ExtensionType.KEY_USAGE)) {
                certGen = setKeyUsage(certGen, dto.getKeyUsageIds(), certificateType);
            } else if (extension.getExtensionType().equals(ExtensionType.EXTENDED_KEY_USAGE)) {
                certGen = setExtendedKeyUsage(certGen, dto.getExtendedKeyUsageIds());
            }
        }
        return certGen;
    }

    private X509v3CertificateBuilder setKeyUsage(X509v3CertificateBuilder certGen, List<Integer> keyUsageIds, String certificateType) throws CertIOException {
        log.debug("SKU with IDS [{}] to NC with TP: {}", keyUsageIds, certificateType);
        int usage = 0;
        for (Integer id : keyUsageIds) {
            bsep.pkiapp.model.KeyUsage keyUsage = keyUsageService.getById(id);

            if (keyUsage.getKeyUsageType().equals(KeyUsageType.DIGITAL_SIGNATURE)) {
                usage |= KeyUsage.digitalSignature;
            } else if(keyUsage.getKeyUsageType().equals(KeyUsageType.NON_REPUDIATION)) {
                usage |= KeyUsage.nonRepudiation;
            } else if (keyUsage.getKeyUsageType().equals(KeyUsageType.KEY_ENCIPHERMENT)) {
                usage |= KeyUsage.keyEncipherment;
            } else if (keyUsage.getKeyUsageType().equals(KeyUsageType.DATE_ENCIPHERMENT)) {
                usage |= KeyUsage.dataEncipherment;
            } else if (keyUsage.getKeyUsageType().equals(KeyUsageType.KEY_AGREEMENT)) {
                usage |= KeyUsage.keyAgreement;
            } else if (keyUsage.getKeyUsageType().equals(KeyUsageType.CERTIFICATE_SIGNING)) {
                usage |= KeyUsage.keyCertSign;
            } else if (keyUsage.getKeyUsageType().equals(KeyUsageType.CRL_SIGNING)) {
                usage |= KeyUsage.cRLSign;
            } else if (keyUsage.getKeyUsageType().equals(KeyUsageType.ENCIPHER_ONLY)) {
                usage |= KeyUsage.encipherOnly;
            } else if (keyUsage.getKeyUsageType().equals(KeyUsageType.DECIPHER_ONLY)) {
                usage |= KeyUsage.decipherOnly;
            }
        }

        if (!certificateType.equals("END_ENTITY"))
            usage |= KeyUsage.keyCertSign;

        certGen.addExtension(Extension.keyUsage, true, new KeyUsage(usage));
        return certGen;
    }

    private X509v3CertificateBuilder setExtendedKeyUsage(X509v3CertificateBuilder certGen, List<Integer> extendedKeyUsageIds) throws CertIOException {
        log.debug("SEKU with IDS: {}", extendedKeyUsageIds);
        List<KeyPurposeId> keyPurposeIds = new ArrayList<>();

        for(Integer id : extendedKeyUsageIds) {
            bsep.pkiapp.model.ExtendedKeyUsage extendedKeyUsage = extendedKeyUsageService.getById(id);

            if(extendedKeyUsage.getExtendedKeyUsageType().equals(ExtendedKeyUsageType.CLIENT_AUTH))
                keyPurposeIds.add(KeyPurposeId.id_kp_clientAuth);
            else if(extendedKeyUsage.getExtendedKeyUsageType().equals(ExtendedKeyUsageType.SERVER_AUTH))
                keyPurposeIds.add(KeyPurposeId.id_kp_serverAuth);
            else if(extendedKeyUsage.getExtendedKeyUsageType().equals(ExtendedKeyUsageType.EMAIL_PROTECTION))
                keyPurposeIds.add(KeyPurposeId.id_kp_emailProtection);
            else if(extendedKeyUsage.getExtendedKeyUsageType().equals(ExtendedKeyUsageType.OCSP_SIGNING))
                keyPurposeIds.add(KeyPurposeId.id_kp_OCSPSigning);
        }

        KeyPurposeId[] extendedKeyUsages = new KeyPurposeId[keyPurposeIds.size()];
        keyPurposeIds.toArray(extendedKeyUsages);

        certGen.addExtension(Extension.extendedKeyUsage, false, new ExtendedKeyUsage(extendedKeyUsages));

        return certGen;
    }

    public List<CertificateExtension> getAll() {
        return extensionRepository.findAll();
    }
}
