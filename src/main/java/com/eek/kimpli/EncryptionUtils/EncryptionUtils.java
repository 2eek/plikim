//package com.eek.kimpli.EncryptionUtils;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//
//public class EncryptionUtils {
//
//    private static final String ALGORITHM = "AES";
//    private static final String SECRET_KEY = generateSecretKey();
//
//    private static String generateSecretKey() {
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(256); // You can choose 128, 192, or 256 bits
//            SecretKey secretKey = keyGenerator.generateKey();
//            byte[] encoded = secretKey.getEncoded();
//            return Base64.getEncoder().encodeToString(encoded);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Key generation failed", e);
//        }
//    }
//
//    public static String encrypt(String value) {
//        try {
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            byte[] encryptedValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
//            return Base64.getEncoder().encodeToString(encryptedValue);
//        } catch (Exception e) {
//            throw new RuntimeException("Encryption failed", e);
//        }
//    }
//
//public static String decrypt(String encryptedValue) {
//    try {
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);
//        byte[] decryptedValue = cipher.doFinal(decodedValue);
//
//        // 디버깅: 암호화된 값 및 복호화된 값을 인쇄
//        System.out.println("암호화된 값: " + encryptedValue);
//        System.out.println("복호화된 값: " + new String(decryptedValue, StandardCharsets.UTF_8));
//
//        return new String(decryptedValue, StandardCharsets.UTF_8);
//    } catch (Exception e) {
//        throw new RuntimeException("복호화 실패", e);
//    }
//}
//}
