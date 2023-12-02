//package com.eek.kimpli.EncryptionUtils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//
//@Slf4j
//@Component
//public class KeyEncrypt {
//
//    private final static String shaAlg = "SHA-256";
//    private final static String aesAlg = "AES/CBC/PKCS5Padding";
//    private final static String key = "MyTestCode-32Character-TestAPIKey";
//    private final static String iv = key.substring(0, 16);
//
//    public String encrypt(String text) {
//        try {
//            String shaKey = getShaKey(text);
//            return getAesKey(shaKey);
//        } catch (Exception e) {
//            throw new RuntimeException("암호화 처리중에 에러가 발생했습니다.", e);
//        }
//    }
//
//	// SHA-256 키 만들기
//    private String getShaKey(String text) throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance(shaAlg);
//        md.update(text.getBytes());
//        return bytesToHex(md.digest());
//    }
//
//	// AES 키 만들기
//// AES 키 만들기
//private String getAesKey(String text) throws Exception {
//    Cipher cipher = Cipher.getInstance(aesAlg);
//
//    // 아래 두 줄을 수정하여 키 길이를 32바이트로 고정
//    byte[] keyBytes = key.substring(0, 32).getBytes();
//    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
//
//    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
//    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
//
//    byte[] encodedBytes = cipher.doFinal(text.getBytes());
//    byte[] encrypted = Base64.getEncoder().encode(encodedBytes);
//    return new String(encrypted).trim();
//}
//
//    private String bytesToHex(byte[] bytes) {
//        StringBuilder builder = new StringBuilder();
//        for (byte b : bytes) {
//            builder.append(String.format("%02x", b));
//        }
//        return builder.toString();
//    }
//
//}
