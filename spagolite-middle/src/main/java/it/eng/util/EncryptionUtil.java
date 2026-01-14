/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import it.eng.spagoCore.error.EMFError;

/**
 *
 * @author MIacolucci
 *
 *         Classe di utilità  per la criptazione/decriptazione di stringhe
 */
public class EncryptionUtil {

    public enum Aes {
        BIT_128, BIT_192, BIT_256
    }

    // Vecchia chiave segreta per calcolare l'HMAC
    private static final String SEC = "dsfjajnoi4jh983nkj43nfkjrenf90rg834jnlkj";
    // Nuove chiavi segrete di dimensioni diverse per calcolare l'AES
    private static final String K128 = "11ARxPczIk6+SygfLcM3Ig==";
    private static final String K192 = "mV7Z89FOzUng7Tzfu/Huzq9Esa2YaSnR";
    private static final String K256 = "iAQvzHBQkGtNt9sQSIEroDMaN+shrDjWN90DpNzKFWY=";

    private EncryptionUtil() {
    }

    public static byte[] encodeBase64(byte[] arr) {
        return Base64.getEncoder().encode(arr);
    }

    public static byte[] decodeBase64(byte[] arr) {
        return Base64.getDecoder().decode(arr);
    }

    public static String getBase64Utf8String(byte[] arr) {
        return new String(arr, StandardCharsets.UTF_8);
    }

    public static byte[] aesCrypt(String plainText) {
        return aesCrypt(plainText, Aes.BIT_128);
    }

    /**
     *
     * @param plainText Stringa da Criptare con algoritmo AES con profondità  di Bit richiesti
     * @param bit       128, 192, 256 bit
     *
     * @return byte array contenente l'input cifrato
     */
    public static byte[] aesCrypt(String plainText, Aes bit) {
        byte[] cipherMessage = null;
        try {
            SecureRandom secureRandom = new SecureRandom();
            String chiave = null;
            switch (bit) {
            case BIT_256:
                chiave = K256;
                break;
            case BIT_192:
                chiave = K192;
                break;
            case BIT_128:
            default:
                chiave = K128;
            }
            SecretKey secretKey = new SecretKeySpec(
                    decodeBase64(chiave.getBytes(StandardCharsets.UTF_8)), "AES");
            byte[] iv = new byte[12];
            secureRandom.nextBytes(iv);
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv); // 128 bit auth tag
            // length
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);
            cipherMessage = byteBuffer.array();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchPaddingException | BadPaddingException | InvalidKeyException
                | IllegalBlockSizeException ex) {
            throw new RuntimeException("Errore nell'encoding AES", ex);
        }
        return cipherMessage;
    }

    public static byte[] aesDecrypt(byte[] cipherMessage) {
        return aesDecrypt(cipherMessage, Aes.BIT_128);
    }

    /**
     *
     * @param cipherMessage byte arrai della stringa cifrata
     * @param bit           128, 192, 256 bit
     *
     * @return byte array contenente la stringa oroginaria
     */
    public static byte[] aesDecrypt(byte[] cipherMessage, Aes bit) {
        byte[] decryptedArray = null;
        try {
            String chiave = null;
            switch (bit) {
            case BIT_256:
                chiave = K256;
                break;
            case BIT_192:
                chiave = K192;
                break;
            case BIT_128:
            default:
                chiave = K128;
            }
            SecretKey secretKey = new SecretKeySpec(
                    decodeBase64(chiave.getBytes(StandardCharsets.UTF_8)), "AES");
            final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            // usa i primi 12 bytes per l'iv
            AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, cipherMessage, 0, 12);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmIv);
            // use everything from 12 bytes on as ciphertext
            decryptedArray = cipher.doFinal(cipherMessage, 12, cipherMessage.length - 12);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchPaddingException | BadPaddingException | InvalidKeyException
                | IllegalBlockSizeException ex) {
            throw new RuntimeException("Errore nel decoding AES", ex);
        }
        return decryptedArray;
    }

    public static void main(String[] args) {
        generateBase64Aes128_192_256BitKey();
        byte[] criptato = aesCrypt("TangerineDream", Aes.BIT_128);
        byte[] descryptato = aesDecrypt(criptato, Aes.BIT_128);
        Logger.getLogger(EncryptionUtil.class.getName()).log(Level.INFO,
                "Stringa   originale: TangerineDream");
        Logger.getLogger(EncryptionUtil.class.getName()).log(Level.INFO,
                "criptata   (base64): " + getBase64Utf8String(encodeBase64(criptato)));
        Logger.getLogger(EncryptionUtil.class.getName()).log(Level.INFO,
                "decriptata (base64): " + getBase64Utf8String(descryptato));
    }

    /**
     * Metodo di utilità  per generare delle chiavi Base64 UTF-8 da mettere come costanti in questa
     * classe
     */
    private static void generateBase64Aes128_192_256BitKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        Logger.getLogger(EncryptionUtil.class.getName()).log(Level.INFO,
                "Chiave 128 Bit (Base64): " + getBase64Utf8String(encodeBase64(key)));
        secureRandom = new SecureRandom();
        byte[] key2 = new byte[24];
        secureRandom.nextBytes(key2);
        Logger.getLogger(EncryptionUtil.class.getName()).log(Level.INFO,
                "Chiave 192 Bit (Base64): " + getBase64Utf8String(encodeBase64(key2)));
        secureRandom = new SecureRandom();
        byte[] key3 = new byte[32];
        secureRandom.nextBytes(key3);
        Logger.getLogger(EncryptionUtil.class.getName()).log(Level.INFO,
                "Chiave 256 Bit (Base64): " + getBase64Utf8String(encodeBase64(key3)));
    }

    /**
     * TOrna il vecchio HMAC di una stringa. Usato da sempre in sacer per passare parametri sulla
     * URL
     *
     * @param msg stringa di cui calcolare l'HMAC
     *
     * @return L'HMAC della stringa passata.
     *
     * @throws EMFError
     */
    public static String getHMAC(String msg) throws EMFError {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SEC.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            byte[] result = mac.doFinal(msg.getBytes());
            return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(result);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new EMFError(ex.getMessage(), ex);
        }
    }

}
