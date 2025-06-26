/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoLite.security.auth;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Quaranta_M
 */
public class PwdUtil {

    private static Logger log = LoggerFactory.getLogger(PwdUtil.class);
    private static final int ITERATIONS = 2048;
    private static final int KEY_LENGTH = 64 * 8;

    public static String encodePassword(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes("UTF-8"), 0, password.length());
            byte[] pwdHash = md.digest();
            return new String(Base64.getEncoder().encode(pwdHash), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException ex) {
            log.error("Algoritmo SHA-1 non supportato");
            throw new RuntimeException(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            log.error("Algoritmo UTF-8 non supportato");
            throw new RuntimeException(ex.getMessage());
        }

    }

    public static String encodePBKDF2Password(byte[] binarySalt, String password) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), binarySalt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] pwdHash = skf.generateSecret(spec).getEncoded();
            return new String(Base64.getEncoder().encode(pwdHash), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException ex) {
            log.error("Algoritmo PBKDF2WithHmacSHA1 non supportato");
            throw new RuntimeException(ex.getMessage());
        } catch (InvalidKeySpecException ex) {
            log.error("Algoritmo PBKDF2WithHmacSHA1 non supportato");
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static String encodeUFT8Base64String(byte[] barray) {
        return new String(Base64.getEncoder().encode(barray), StandardCharsets.UTF_8);
    }

    public static byte[] decodeUFT8Base64String(String utf8str) {
        return Base64.getDecoder().decode(utf8str.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] generateSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstanceStrong();
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException ex) {
            log.error("Algoritmo SHA1PRNG non supportato");
            throw new RuntimeException(ex.getMessage());
        }
    }
}
