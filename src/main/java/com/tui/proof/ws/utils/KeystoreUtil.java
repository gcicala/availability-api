/**
 * 
 */
package com.tui.proof.ws.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 11-gen-2021
 * 
 * @Date : 11-gen-2021
 * 
 * @Time : 11.54.20
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.utils.KeystoreUtil
 * 
 */
@Log4j2
public class KeystoreUtil {

	public static X509Certificate getCertificate(
			String alias,
			KeyStore keyStore) throws Throwable {
		return (X509Certificate) keyStore.getCertificate(alias);

	}

	public static PublicKey getPublicKey(
			String alias,
			KeyStore keyStore) throws Throwable {
		return keyStore.getCertificate(alias).getPublicKey();

	}

	public static final PrivateKey getPrivateKey(
			String alias,
			KeyStore keystore,
			String keyPassword) throws Throwable {
		KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(keyPassword.toCharArray());
		PrivateKeyEntry entry = (PrivateKeyEntry) keystore.getEntry(alias, entryPassword);
		return entry.getPrivateKey();
	}

	public static KeyStore loadKeystore(
			String fileName,
			KeystoreType storeType,
			String keyPassword) {
		try {
			KeyStore keyStore = KeyStore.getInstance(storeType.name());
			fileName += "." + storeType.name();
			File keystore = Paths.get(fileName).toFile();
			if (keystore.exists()) {
				try (FileInputStream fis = new FileInputStream(keystore)) {
					keyStore.load(fis, keyPassword.toCharArray());
				}
			} else {
				keyStore.load(null, null);
			}
			return keyStore;
		} catch (Exception ex) {
			log.error(String.format("error loading the keystore [%s]", Stream.of(ex.getStackTrace()).map(stack -> " --> " + stack.toString()).collect(Collectors.joining("\n"))));
			throw new RuntimeException(ex);
		}
	}

	@Getter
	public enum KeystoreType {
		jceks("SunJCE"), jks("SUN"), dks("BC"), pkcs11("BC"), pkcs12("BC");

		private String provider;

		KeystoreType(
			String provider) {
			this.provider = provider;
		}
	}

	public static void writeCertToFileBase64Encoded(
			Certificate certificate,
			String fileName) throws Exception {
		try (FileOutputStream certificateOut = new FileOutputStream(fileName)) {
			certificateOut.write("-----BEGIN CERTIFICATE-----".getBytes());
			certificateOut.write(Base64.getEncoder().encode(certificate.getEncoded()));
			certificateOut.write("-----END CERTIFICATE-----".getBytes());
		}
	}

	public static void writePrivateKeyToFileBase64Encoded(
			PrivateKey privKey,
			String fileName) throws Exception {
		File privKeyFile = Paths.get(fileName).toFile();
		try (FileOutputStream certificateOut = new FileOutputStream(privKeyFile)) {
			certificateOut.write("-----BEGIN PRIVATE KEY-----".getBytes());
			certificateOut.write(Base64.getEncoder().encode(privKey.getEncoded()));
			certificateOut.write("-----END PRIVATE KEY-----".getBytes());
		}
	}

	public static void writePublicKeyToFileBase64Encoded(
			PublicKey pubKey,
			String fileName) throws IOException {
		File pubKeyFile = Paths.get(fileName).toFile();
		try (FileOutputStream certificateOut = new FileOutputStream(pubKeyFile)) {
			certificateOut.write("-----BEGIN PUBLIC KEY-----".getBytes());
			certificateOut.write(Base64.getEncoder().encode(pubKey.getEncoded()));
			certificateOut.write("-----END PUBLIC KEY-----".getBytes());
		}
	}

	public static void main(
			String[] args) {
		try {
			KeyStore keystore = loadKeystore("./src/main/resources/keystore", KeystoreType.pkcs12, "jwtPassword");

			X509Certificate certificate = getCertificate("jwtalias", keystore);
			PrivateKey privateKey = getPrivateKey("jwtalias", keystore, "jwtPassword");
			PublicKey publicKey = getPublicKey("jwtalias", keystore);
			writePrivateKeyToFileBase64Encoded(privateKey, "jwtaliasPriv.key");
			writePublicKeyToFileBase64Encoded(publicKey, "jwtaliasPub.key");
			writeCertToFileBase64Encoded(certificate, "jwtaliasCert.crt");

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
