package bsep.pkiapp.service;

import java.security.PrivateKey;
import java.security.cert.Certificate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import bsep.pkiapp.keystores.KeyStoreWriter;

@Service
@Slf4j
public class KeyStoreService {

	public void writeRootCertificateToKeyStore(String serialNumber, PrivateKey privateKey, Certificate certificate, String password) {
		log.debug("Write root certificate to key store with serial number: {}", serialNumber);
		KeyStoreWriter ksw = new KeyStoreWriter();
		ksw.loadKeyStore(null, password.toCharArray());
		ksw.write(serialNumber, privateKey, password.toCharArray(), new Certificate[]{certificate});
		ksw.saveKeyStore(".\\files\\root" + serialNumber + ".jks", password.toCharArray());
		
		KeyStoreWriter kswHierarchy = new KeyStoreWriter();
		kswHierarchy.loadKeyStore(null, password.toCharArray());
		kswHierarchy.saveKeyStore(".\\files\\hierarchy" + serialNumber + ".jks", password.toCharArray());
	}
    
    public void writeCertificateToHierarchyKeyStore(String serialNumber, String rootSerialNumber,
													PrivateKey privateKey, Certificate[] certificates,
													String password) {
		log.debug("Write certificate with serial number [{}] to hierarchy key store with serial number [{}]", serialNumber, rootSerialNumber);
		KeyStoreWriter ksw = new KeyStoreWriter();
		ksw.loadKeyStore(".\\files\\hierarchy" + rootSerialNumber + ".jks", rootSerialNumber.toString().toCharArray());
		ksw.write(serialNumber, privateKey, rootSerialNumber.toString().toCharArray(), certificates);
		ksw.saveKeyStore(".\\files\\hierarchy" + rootSerialNumber + ".jks", rootSerialNumber.toString().toCharArray());
	}

}
