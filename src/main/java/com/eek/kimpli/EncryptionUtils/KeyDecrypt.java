//package com.eek.kimpli.EncryptionUtils;
//
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Base64;
//import java.util.Arrays;
//
//@Component
//public class KeyDecrypt {
//
//    private final static String alg = "AES/CBC/PKCS5Padding";
//    private final static String key = "MyTestCode-32Character-TestAPIKey";
//    private final static String iv = key.substring(0, 16);
//
//    public String decrypt(String clientKey) {
//        try {
//            Cipher cipher = Cipher.getInstance(alg);
//
//            // key를 32바이트로 만들고 IV를 사용하여 초기화
//            byte[] keyBytes = Arrays.copyOf(key.getBytes(), 32);
//            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
//            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
//            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
//
//            // Base64 디코딩 후 복호화
//            byte[] decodedBytes = Base64.getDecoder().decode(clientKey);
//            byte[] decrypted = cipher.doFinal(decodedBytes);
//
//            // 추가: 디버깅 정보 출력
//            System.out.println("Decryption successful");
//            System.out.println("Decrypted message: " + new String(decrypted));
//
//            return new String(decrypted).trim();
//        } catch (Exception e) {
//            // 추가: 디버깅 정보 출력
//            System.err.println("Decryption error: " + e.getMessage());
//            throw new RuntimeException("복호화 처리중에 에러가 발생했습니다.", e);
//        }
//    }
//}
