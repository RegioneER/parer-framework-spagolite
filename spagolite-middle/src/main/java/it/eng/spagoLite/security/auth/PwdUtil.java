package it.eng.spagoLite.security.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Quaranta_M
 */
public class PwdUtil {

    private static Logger log = LoggerFactory.getLogger(PwdUtil.class);
    private final static int ITERATIONS = 2048;
    private final static int KEY_LENGTH = 64 * 8;

    @Deprecated
    public static String encodePassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes("UTF-8"), 0, password.length());
            byte[] pwdHash = md.digest();
            return new String(Base64.encodeBase64(pwdHash), "UTF-8");
        } catch (NoSuchAlgorithmException ex) {
            log.error("Algoritmo SHA-1 non supportato");
            throw new RuntimeException(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            log.error("Algoritmo UTF-8 non supportato");
            throw new RuntimeException(ex.getMessage());
        }

    }

    public static void main(String[] args) {
        // String encodePBKDF2Password = encodePBKDF2Password(null, "password");
        // System.out.println(encodePBKDF2Password);

        String encodePassword = encodePassword("password");
        System.out.println(encodePassword);
    }

    public static String encodePBKDF2Password(byte[] binarySalt, String password) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), binarySalt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] pwdHash = skf.generateSecret(spec).getEncoded();
            return new String(Base64.encodeBase64(pwdHash), "UTF-8");
        } catch (NoSuchAlgorithmException ex) {
            log.error("Algoritmo PBKDF2WithHmacSHA1 non supportato");
            throw new RuntimeException(ex.getMessage());
        } catch (InvalidKeySpecException ex) {
            log.error("Algoritmo PBKDF2WithHmacSHA1 non supportato");
            throw new RuntimeException(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            log.error("Algoritmo UTF-8 non supportato");
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static String encodeUFT8Base64String(byte[] barray) {
        try {
            return new String(Base64.encodeBase64(barray), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error("Algoritmo UTF-8 non supportato");
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static byte[] decodeUFT8Base64String(String utf8str) {
        try {
            return Base64.decodeBase64(utf8str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            log.error("Algoritmo UTF-8 non supportato");
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static byte[] generateSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException ex) {
            log.error("Algoritmo SHA1PRNG non supportato");
            throw new RuntimeException(ex.getMessage());
        }
    }
}
