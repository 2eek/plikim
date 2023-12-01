package com.eek.kimpli.EncryptionUtils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtils {

    private static final String SECRET_KEY = generateRandomKey(); // 무작위의 시크릿 키
    private static final String INIT_VECTOR = generateRandomIV(); // 무작위의 초기화 벡터

    private static String generateRandomKey() {
        byte[] keyBytes = new byte[16]; // 128 bits = 16 bytes
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    private static String generateRandomIV() {
        byte[] ivBytes = new byte[16]; // 128 bits = 16 bytes
        new SecureRandom().nextBytes(ivBytes);
        return Base64.getEncoder().encodeToString(ivBytes);
    }

    public static String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(INIT_VECTOR));

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(INIT_VECTOR));

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
