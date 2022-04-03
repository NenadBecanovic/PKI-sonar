package bsep.pkiapp.service;

import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.utils.X500NameGenerator;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

@Service
public class CertificateService {

    @Autowired
    private X500NameGenerator x500NameGenerator;


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
                    new BigInteger(String.valueOf(abs(ThreadLocalRandom.current().nextInt()))),
                    new Date(),
                    dto.getValidityEndDate(),
                    subject,
                    keyPair.getPublic());

            X509CertificateHolder certHolder = certGen.build(contentSigner);

            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            X509Certificate certificate = certConverter.getCertificate(certHolder);

        } catch (IllegalArgumentException | IllegalStateException
                | OperatorCreationException | CertificateException e) {
            e.printStackTrace();
        }
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

}
