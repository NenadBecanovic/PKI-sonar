package bsep.pkiapp.service;

import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.keystores.KeyStoreReader;
import bsep.pkiapp.keystores.KeyStoreWriter;
import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.model.CertificateType;
import bsep.pkiapp.repository.CertificateChainRepository;
import bsep.pkiapp.utils.X500NameGenerator;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
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
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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

	@Autowired
	private KeyStoreService keyStoreService;

	@Autowired
	private CertificateChainRepository certificateChainRepository;

	public void createCertificate(NewCertificateDto dto) {
		X500Name subject = x500NameGenerator.generateX500Name(dto);
		if (dto.certificateType.equals("ROOT")) {
			generateCertificate(dto, subject, subject);
		} else {
			// TODO: get issuer from KeyStorage, check if issuer is of role: ROLE_CA
			if (isIssuerRootCertificate(dto.issuerSerialNumber)) {
				KeyStoreReader keyStore = new KeyStoreReader();
				X500Name x500NameIssuer = keyStore.readIssuerNameFromStore(
						".\\files\\root" + dto.issuerSerialNumber + ".jks", dto.issuerSerialNumber,
						dto.issuerSerialNumber.toCharArray(), dto.issuerSerialNumber.toCharArray());
				generateCertificate(dto, x500NameIssuer, subject);
			} else {
				KeyStoreReader keyStore = new KeyStoreReader();
				String rootSerialNumber = findRootSerialNumberByIssuerSerialNumber(dto.issuerSerialNumber);
				X500Name x500NameIssuer = keyStore.readIssuerNameFromStore(
						".\\files\\hierarchy" + rootSerialNumber + ".jks", dto.issuerSerialNumber,
						rootSerialNumber.toCharArray(), dto.issuerSerialNumber.toCharArray());
				generateCertificate(dto, x500NameIssuer, subject);
			}
		}
	}

	private String findRootSerialNumberByIssuerSerialNumber(String issuerSerialNumber) {
		CertificateChain certificate = certificateChainRepository.getById(Long.decode(issuerSerialNumber));
		return findRootSerialNumber(certificate);
	}

	private boolean isIssuerRootCertificate(String issuerSerialNumber) {
		CertificateChain certificate = certificateChainRepository.getById(Long.decode(issuerSerialNumber));
		return (CertificateType.ROOT).equals(certificate.getCertificateType());
	}

	public void generateCertificate(NewCertificateDto dto, X500Name issuer, X500Name subject) {
		try {
			// TODO: validation checks, keyStorage, DataBase storage, extensions and
			// purposes
			JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
			builder = builder.setProvider("BC");
			KeyStoreReader ksr = new KeyStoreReader();
			KeyPair keyPair = generateKeyPair();

			ContentSigner contentSigner;
			if (dto.certificateType.equals("ROOT")) {
				contentSigner = builder.build(keyPair.getPrivate());
			} else {
				PrivateKey privateKey = null;
				if (isIssuerRootCertificate(dto.issuerSerialNumber)) {
					privateKey = ksr.readIssuerFromStore(".\\files\\root" + dto.issuerSerialNumber + ".jks",
							dto.issuerSerialNumber, dto.issuerSerialNumber.toCharArray(),
							dto.issuerSerialNumber.toCharArray());
				} else {
					String rootSerialNumber = findRootSerialNumberByIssuerSerialNumber(dto.issuerSerialNumber);
					privateKey = ksr.readIssuerFromStore(".\\files\\hierarchy" + rootSerialNumber + ".jks",
							dto.issuerSerialNumber, rootSerialNumber.toCharArray(),
							dto.issuerSerialNumber.toCharArray());
				}
				contentSigner = builder.build(privateKey);
			}

			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuer, generateSerialNumber(),
					new Date(), dto.getValidityEndDate(), subject, keyPair.getPublic());
			dto.extensionSettingsDto.setPublicKey(keyPair.getPublic());
			dto.extensionSettingsDto.setSubjectName(subject);

			certGen = extensionService.addExtensions(certGen, dto.getExtensionSettingsDto(), dto.getCertificateType());
			X509CertificateHolder certHolder = certGen.build(contentSigner);

			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");
			X509Certificate certificate = certConverter.getCertificate(certHolder);
			if (dto.certificateType.equals("ROOT")) {
				Date startDate = new Date();
				CertificateChain chain = new CertificateChain(new BigInteger("0"), dto.organizationName, CertificateType.ROOT, null,
						startDate, dto.validityEndDate, true);
				certificateChainRepository.save(chain);
				keyStoreService.writeRootCertificateToKeyStore(chain.getSerialNumber().toString(), keyPair.getPrivate(),
						certificate, chain.getSerialNumber().toString());
			} else if(dto.certificateType.equals("INTERMEDIATE")) {
				Date startDate = new Date();
				CertificateChain chain = new CertificateChain(new BigInteger("0"), dto.organizationUnit, CertificateType.INTERMEDIATE, null,
						startDate, dto.validityEndDate, true);
				String rootSerialNumber = findRootSerialNumber(chain);
				certificateChainRepository.save(chain);
				keyStoreService.writeCertificateToHierarchyKeyStore(chain.getSerialNumber().toString(),
						rootSerialNumber, keyPair.getPrivate(), certificate, chain.getSerialNumber().toString());
			} else {
				Date startDate = new Date();
				//TO DO: Add user info to CertificateChain instead of organization unit 
				CertificateChain chain = new CertificateChain(new BigInteger("0"), dto.organizationUnit, CertificateType.END_ENTITY, null,
						startDate, dto.validityEndDate, true);
				String rootSerialNumber = findRootSerialNumber(chain);
				certificateChainRepository.save(chain);
				keyStoreService.writeCertificateToHierarchyKeyStore(chain.getSerialNumber().toString(),
						rootSerialNumber, keyPair.getPrivate(), certificate, chain.getSerialNumber().toString());
			}
		} catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException
				| NoSuchAlgorithmException | IOException e) {
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

	private String findRootSerialNumber(CertificateChain certificate) {
		while (!(CertificateType.ROOT).equals(certificate.getCertificateType())) {
			certificate = certificateChainRepository
					.getCertificateChainBySignerSerialNumber(certificate.getSignerSerialNumber());
		}
		return certificate.getSerialNumber().toString();
	}

	public byte[] getCertificate(Long serialNumber) throws CertificateEncodingException {
		byte[] crt = getX509Certificate(serialNumber).getEncoded();
		return crt;
	}

	private X509Certificate getX509Certificate(Long serialNumber) {
		KeyStoreReader ksr = new KeyStoreReader();
		X509Certificate certificate = null;
		CertificateChain certificateChain = certificateChainRepository.findById(serialNumber).orElseThrow();
		if ((CertificateType.ROOT).equals(certificateChain.getCertificateType())) {
			certificate = (X509Certificate) ksr.readCertificate(".\\files\\root" + serialNumber.toString() + ".jks",
					serialNumber.toString(), serialNumber.toString());
		} else {
			String rootSerialNumber = findRootSerialNumber(certificateChain);
			certificate = (X509Certificate) ksr.readCertificate(".\\files\\hierarchy" + rootSerialNumber + ".jks",
					rootSerialNumber, serialNumber.toString());
		}

		return certificate;
	}
}
