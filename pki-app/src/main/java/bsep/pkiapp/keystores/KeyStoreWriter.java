package bsep.pkiapp.keystores;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

@Slf4j
public class KeyStoreWriter {
	//KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
	//Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
	// - Sertifikati koji ukljucuju javni kljuc
	// - Privatni kljucevi
	// - Tajni kljucevi, koji se koriste u simetricnima siframa
	private KeyStore keyStore;
	
	public KeyStoreWriter() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
		} catch (KeyStoreException | NoSuchProviderException e) {
			e.printStackTrace();
		}
	}
	
	public void loadKeyStore(String fileName, char[] password) {
		try {
			log.debug("Load key store: {}", fileName);
			if(fileName != null) {
				keyStore.load(new FileInputStream(fileName), password);
			} else {
				//Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
				keyStore.load(null, password);
			}
		} catch (CertificateException | NoSuchAlgorithmException | IOException e) {
			log.warn("Failed to load key store: {}", fileName);
			e.printStackTrace();
		}
	}
	
	public void saveKeyStore(String fileName, char[] password) {
		try {
			log.debug("Save key store: {}", fileName);
			keyStore.store(new FileOutputStream(fileName), password);
		} catch (NoSuchAlgorithmException | KeyStoreException | IOException | CertificateException e) {
			log.warn("Failed to save key store: {}", fileName);
			e.printStackTrace();
		}
	}
	
	public void write(String alias, PrivateKey privateKey, char[] password, Certificate[] certificates) {
		try {
			log.debug("Write to key store with alias: {}", alias);
			keyStore.setKeyEntry(alias, privateKey, password, certificates);
		} catch (KeyStoreException e) {
			log.warn("Failed to write to key store with alias: {}", alias);
			e.printStackTrace();
		}
	}
}

