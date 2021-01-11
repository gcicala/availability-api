/**
 * 
 */
package com.tui.proof.ws.security.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.tui.proof.ws.exception.ServiceException;
import com.tui.proof.ws.security.entity.JwtTokenBookingHolderRole;
import com.tui.proof.ws.security.service.JwtTokenUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 8-gen-2021
 * 
 * @Date : 8-gen-2021
 * 
 * @Time : 10.51.48
 * 
 * @Project : availability-api
 * 
 * @Class : com.tui.proof.ws.security.JwtTokenProvider
 * 
 */
@Component
@Log4j2
@Data
public class JwtTokenProvider {

	/**
	 * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key
	 * here. Ideally, in a microservices environment, this key would be kept on a
	 * config-server.
	 */

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h

	@Autowired
	private JwtTokenUserDetailsService<?> tokenUserDetails;

	@Value("${security.jwt.token.keystore.file:keystore.pkcs12}")
	private Path keyStoreFileName;

	@Value("${security.jwt.token.keystore.alias:jwtalias}")
	private String jwtCertAlias;

	@Value("${security.jwt.token.keystore.password:jwtPassword}")
	private String keyStorePassword;

	@Value("${security.jwt.token.verifySignature:true}")
	private boolean verifySignature;

	private PublicKey publicKey;

	private PrivateKey privateKey;

	@PostConstruct
	protected void init() {
		if (verifySignature) {
			Path path = keyStoreFileName.resolve(keyStoreFileName.toAbsolutePath());
			File keyStoreFile = path.toFile();
			try (FileInputStream fi = new FileInputStream(keyStoreFile)) {
				KeyStore keyStore = loadKeyStore(fi, keyStorePassword.toCharArray());
				log.info("Keystore [" + keyStoreFileName + "] loaded");
				publicKey = getPublicKey(keyStore, jwtCertAlias);
				privateKey = getPrivateKey(keyStore, jwtCertAlias);
				log.info("Jwt public key: " + publicKey);
				fi.close();
			} catch (Exception ex) {
				log.error(String.format("error creating the JwtTokenProviderUtils [%s]", Stream.of(ex.getStackTrace()).map(stack -> " --> " + stack.toString()).collect(Collectors.joining("\n"))));
				log.error("JwtTokenProvider validation DISABLED");
				this.verifySignature = false;
			}

		}
	}

	private final PrivateKey getPrivateKey(
			KeyStore keystore,
			String alias) throws Exception {
		KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());
		PrivateKeyEntry entry = (PrivateKeyEntry) keystore.getEntry(alias, entryPassword);

		return entry.getPrivateKey();
	}

	public PublicKey getPublicKey(
			KeyStore keystore,
			String alias) throws Exception {
		X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);

		PublicKey pk = certificate.getPublicKey();
		return pk;
	}

	public String createToken(
			String username,
			List<JwtTokenBookingHolderRole> roles) {

		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.RS512, getPrivateKey())//
				.compact();
	}

	public Authentication getAuthentication(
			String token) {
		UserDetails userDetails = tokenUserDetails.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(
			String token) {
		return Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(
			HttpServletRequest req) {
		String bearerToken = req.getHeader("jwtToken");
		return bearerToken;
	}

	public boolean validateToken(
			String token) throws ServiceException {
		try {
			if (verifySignature)
				Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new ServiceException("Expired or invalid JWT token", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private KeyStore loadKeyStore(
			InputStream inputStream,
			char[] KEYSTORE_PASSWORD) throws IOException {
		try {
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(inputStream, KEYSTORE_PASSWORD);
			return keyStore;
		} catch (Exception ex) {
			log.error(String.format("error loading the keystore [%s]", Stream.of(ex.getStackTrace()).map(stack -> " --> " + stack.toString()).collect(Collectors.joining("\n"))));
			throw new RuntimeException(ex);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}

		}
	}

}