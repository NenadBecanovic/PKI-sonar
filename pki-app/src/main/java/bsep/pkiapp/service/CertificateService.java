package bsep.pkiapp.service;

import bsep.pkiapp.dto.CertificateDto;
import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.keystores.KeyStoreReader;
import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.model.CertificateType;
import bsep.pkiapp.model.User;
import bsep.pkiapp.repository.CertificateChainRepository;
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

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;


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

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Transactional
	public void createCertificate(NewCertificateDto dto) {
		X500Name subject = x500NameGenerator.generateX500Name(dto);
		if (dto.getCertificateType().equals("ROOT")) {
			generateCertificate(dto, subject, subject);
		} else {
			// TODO: get issuer from KeyStorage, check if issuer is of role: ROLE_CA
			if (isIssuerRootCertificate(new BigInteger(dto.getIssuerSerialNumber()))) {
				KeyStoreReader keyStore = new KeyStoreReader();
				X500Name x500NameIssuer = keyStore.readIssuerNameFromStore(
						".\\files\\root" + dto.getIssuerSerialNumber() + ".jks", dto.getIssuerSerialNumber().toString(),
						dto.getIssuerSerialNumber().toString().toCharArray(), dto.getIssuerSerialNumber().toString().toCharArray());
				generateCertificate(dto, x500NameIssuer, subject);
			} else {
				KeyStoreReader keyStore = new KeyStoreReader();
				String rootSerialNumber = findRootSerialNumberByIssuerSerialNumber(dto.getIssuerSerialNumber().toString());
				X500Name x500NameIssuer = keyStore.readIssuerNameFromStore(
						".\\files\\hierarchy" + rootSerialNumber + ".jks", dto.getIssuerSerialNumber().toString(),
						rootSerialNumber.toCharArray(), rootSerialNumber.toCharArray());
				generateCertificate(dto, x500NameIssuer, subject);
			}
		}
	}

	private String findRootSerialNumberByIssuerSerialNumber(String issuerSerialNumber) {
		CertificateChain certificate = certificateChainRepository.getBySerialNumber(new BigInteger(issuerSerialNumber));
		return findRootSerialNumber(certificate);
	}

	private boolean isIssuerRootCertificate(BigInteger issuerSerialNumber) {
		CertificateChain certificate = certificateChainRepository.getBySerialNumber(issuerSerialNumber);
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
			User user = userService.getByEmail(dto.getSubjectEmail());

			ContentSigner contentSigner;
			String rootSerialNumber = null;
			if (dto.getCertificateType().equals("ROOT")) {
				contentSigner = builder.build(keyPair.getPrivate());
			} else {
				PrivateKey privateKey = null;
				if (isIssuerRootCertificate(new BigInteger(dto.getIssuerSerialNumber()))) {
					privateKey = ksr.readPrivateKey(".\\files\\root" + dto.getIssuerSerialNumber() + ".jks",
							dto.getIssuerSerialNumber().toString(), dto.getIssuerSerialNumber().toString(),
							dto.getIssuerSerialNumber().toString());
				} else {
					rootSerialNumber = findRootSerialNumberByIssuerSerialNumber(dto.getIssuerSerialNumber().toString());
					privateKey = ksr.readPrivateKey(".\\files\\hierarchy" + rootSerialNumber + ".jks",
							rootSerialNumber, dto.getIssuerSerialNumber().toString(),
							rootSerialNumber);
				}
				contentSigner = builder.build(privateKey);
			}

			BigInteger serialNumber = generateSerialNumber();

			X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuer, serialNumber,
					new Date(), dto.getValidityEndDate(), subject, keyPair.getPublic());
			dto.getExtensionSettingsDto().setPublicKey(keyPair.getPublic());
			dto.getExtensionSettingsDto().setSubjectName(subject);

			PublicKey issuerPublicKey = getIssuerPublicKey(dto, ksr, rootSerialNumber);
			certGen = extensionService.addExtensions(certGen, dto.getExtensionSettingsDto(), dto.getCertificateType()
					, issuerPublicKey);

			X509CertificateHolder certHolder = certGen.build(contentSigner);

			JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
			certConverter = certConverter.setProvider("BC");
			X509Certificate certificate = certConverter.getCertificate(certHolder);
			if (dto.getCertificateType().equals("ROOT")) {
				Date startDate = new Date();
				CertificateChain chain = new CertificateChain(serialNumber, serialNumber, dto.getOrganizationName(),
						CertificateType.ROOT, user,
						startDate, dto.getValidityEndDate(), true);
				certificateChainRepository.save(chain);
				keyStoreService.writeRootCertificateToKeyStore(chain.getSerialNumber().toString(), keyPair.getPrivate(),
						certificate, chain.getSerialNumber().toString());
			} else {
				CertificateChain chain;
				if (dto.getCertificateType().equals("INTERMEDIATE")) {
					chain = new CertificateChain(serialNumber, new BigInteger(dto.getIssuerSerialNumber()),
							dto.getOrganizationUnit(),
							CertificateType.INTERMEDIATE, user,
							new Date(), dto.getValidityEndDate(), true);
				} else {
					// TODO: Add user info to CertificateChain instead of organization unit
					chain = new CertificateChain(serialNumber,
							new BigInteger(dto.getIssuerSerialNumber()), user.getEmail(),
							CertificateType.END_ENTITY, user,
							new Date(), dto.getValidityEndDate(), false);

				}
				String rootSerialNumber2 = findRootSerialNumber(chain);
				certificateChainRepository.save(chain);

//				keyStoreService.writeCertificateToHierarchyKeyStore(chain.getSerialNumber().toString(),
//						rootSerialNumber2, keyPair.getPrivate(), certificate, rootSerialNumber2);
				keyStoreService.writeCertificateToHierarchyKeyStore(chain.getSerialNumber().toString(),
						rootSerialNumber2, keyPair.getPrivate(),
						findCertificateChainHierarchy(certificate, dto.getIssuerSerialNumber(), rootSerialNumber2),
						rootSerialNumber2);
		}

			if (dto.getCertificateType().equals("CA") && user.getRole().getId() == 1) {
				user.setRole(roleService.getById(2));
			}

		} catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException
				| NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
		}
	}

	private PublicKey getIssuerPublicKey(NewCertificateDto dto, KeyStoreReader ksr, String rootSerialNumber) {
		PublicKey issuerPublicKey;
		if(dto.getIssuerSerialNumber() == null){
			issuerPublicKey = dto.getExtensionSettingsDto().getPublicKey();
		}else{
			if(rootSerialNumber == null){
				issuerPublicKey = ksr.readCertificate(".\\files\\root" + dto.getIssuerSerialNumber() + ".jks",
						dto.getIssuerSerialNumber(),
						dto.getIssuerSerialNumber()).getPublicKey();
			}else{
				issuerPublicKey = ksr.readCertificate(".\\files\\hierarchy" + rootSerialNumber + ".jks",
						rootSerialNumber,
						dto.getIssuerSerialNumber()).getPublicKey();
			}
		}
		return issuerPublicKey;
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
		while (!Objects.equals(certificate.getSerialNumber(), certificate.getSignerSerialNumber())) {
			certificate = certificateChainRepository
					.getBySerialNumber(certificate.getSignerSerialNumber());
		}
		return certificate.getSerialNumber().toString();
	}

	private X509Certificate[] findCertificateChainHierarchy(X509Certificate newCertificate, String issuerSerialNumber,
															String rootSerialNumber) {
		X509Certificate certificate = getX509Certificate(new BigInteger(issuerSerialNumber));
		List<X509Certificate> certificateChainHierarchy = new ArrayList<>();
		certificateChainHierarchy.add(newCertificate);
		certificateChainHierarchy.add(certificate);

		String currentSerialNumber = issuerSerialNumber;
		while(!certificate.getSerialNumber().equals(new BigInteger(rootSerialNumber))){
			currentSerialNumber =
					certificateChainRepository.getBySerialNumber(new BigInteger(currentSerialNumber)).getSignerSerialNumber().toString();
			certificate = getX509Certificate(new BigInteger(currentSerialNumber));
			certificateChainHierarchy.add(certificate);
		}
		X509Certificate[] certificateChainHierarchyArray = new X509Certificate[certificateChainHierarchy.size()];
		return certificateChainHierarchy.toArray(certificateChainHierarchyArray);
	}

	public byte[] getCertificate(BigInteger serialNumber) throws CertificateEncodingException {
		byte[] crt = getX509Certificate(serialNumber).getEncoded();
		return crt;
	}

	private X509Certificate getX509Certificate(BigInteger serialNumber) {
		KeyStoreReader ksr = new KeyStoreReader();
		X509Certificate certificate = null;
		CertificateChain certificateChain = certificateChainRepository.getBySerialNumber(serialNumber);
		if ((CertificateType.ROOT).equals(certificateChain.getCertificateType())) {
			certificate = (X509Certificate) ksr.readCertificate(".\\files\\root" + serialNumber.toString() + ".jks",
					serialNumber.toString(), serialNumber.toString());
		} else {
			String rootSerialNumber = findRootSerialNumber(certificateChain);
			certificate = (X509Certificate) ksr.readCertificate(".\\files\\hierarchy" + rootSerialNumber + ".jks",
					rootSerialNumber, serialNumber.toString());
		}
		System.out.println("FUNKCJIA");
		System.out.println(certificate.getSerialNumber());

		return certificate;
	}

	public List<CertificateDto> getRootCertificates() {
		List<CertificateDto> certificates = new ArrayList<>();
		for (CertificateChain cert : certificateChainRepository.getByCertificateType(CertificateType.ROOT)) {
			certificates.add(new CertificateDto(cert));
		}
		return certificates;
	}

	public List<CertificateDto> getAllByUser(String token) {
		User user = userService.getUserFromToken(token);
		List<CertificateDto> dtos;
		if (user.getRole().getAuthority().equals("ROLE_ADMIN")) {
			dtos = convertChainToDto(certificateChainRepository.findAll());
		} else {
			dtos = convertChainToDto(certificateChainRepository.getByUser(user));
		}
		return dtos;
	}

	private List<CertificateDto> convertChainToDto(List<CertificateChain> all) {
		List<CertificateDto> dtos = new ArrayList<>();
		for (CertificateChain chain : all) {
			dtos.add(new CertificateDto(chain));
		}
		return dtos;
	}

	public List<CertificateDto> searchCertificates(String token, String searchText) {
		String finalSearchText = searchText.toLowerCase().trim();
		return getAllByUser(token).stream().filter(cert -> cert.issuer.toLowerCase().trim().contains(finalSearchText)
				|| cert.subject.toLowerCase().trim().contains(finalSearchText)).collect(Collectors.toList());
	}

	public List<CertificateDto> filterCertificates(String token, String filter) {
		return getAllByUser(token).stream().filter(cert -> cert.type.equals(filter)).collect(Collectors.toList());
	}
}
