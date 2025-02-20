package com.interswitchgroup.accountmanagementsystem.common.config.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * @author Joy Osayi
 * @createdOn April-12(Fri)-2024
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {
  @Value("${account-service.securityJWTKeyStorePath}")
  private String keyStorePath;

  @Value("${account-service.securityJWTKeyStorePassword}")
  private String keyStorePassword;

  @Value("${account-service.securityJWTKeyAlias}")
  private String keyAlias;

  @Value("${account-service.securityJWTPrivateKeyPassphrase}")
  private String privateKeyPassphrase;

  @Bean
  public KeyStore keyStore() {
    try {
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      Path path = Paths.get(keyStorePath);
      InputStream resourceAsStream = new FileInputStream(path.toFile());
      keyStore.load(
              resourceAsStream, keyStorePassword.toCharArray());
      return keyStore;
    } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
      log.error("Unable to load keystore: {}", keyStorePath, e);
    }

    throw new IllegalArgumentException("Unable to load keystore");
  }

  @Bean
  public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
    try {
      Key key =
              keyStore.getKey(
                      keyAlias,
                      privateKeyPassphrase.toCharArray());
      if (key instanceof RSAPrivateKey rsaPrivateKey) {
        return rsaPrivateKey;
      }
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
      log.error(
              "Unable to load private key from keystore: {}",
              keyStorePath,
              e);
    }

    throw new IllegalArgumentException("Unable to load private key");
  }

  @Bean
  public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
    try {
      Certificate certificate = keyStore.getCertificate(keyAlias);
      PublicKey publicKey = certificate.getPublicKey();

      if (publicKey instanceof RSAPublicKey rsaPublicKey) {
        return rsaPublicKey;
      }
    } catch (KeyStoreException e) {
      log.error(
              "Unable to load private key from keystore: {}",
             keyStorePath,
              e);
    }

    throw new IllegalArgumentException("Unable to load RSA public key");
  }

  @Bean
  public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
    return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
  }
}
