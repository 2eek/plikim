package com.eek.kimpli.user.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@Component
public class ImageDownloader {



    private static String generateFileName(String imageUrl) {
        // 파일명을 UUID를 이용하여 랜덤하게 생성
        return UUID.randomUUID().toString() + ".jpg";
    }

  public static String imgServerSave(String imageUrl) {
    String savePath = "/home/serve/plikim_img/user_profileImg/";

    try {
        // 이미지 다운로드
        String fileName = downloadImage(imageUrl, savePath);
        // 다운로드 성공 시 fileName 반환
        return fileName;
    } catch (IOException e) {
        e.printStackTrace();
        // 다운로드 실패 시 예외 처리 (또는 다른 방식으로 처리)
        return null; // 또는 적절한 값을 반환
    }
}

    private static String downloadImage(String imageUrl, String savePath) throws IOException {
        URL url = new URL(imageUrl);

        // 이미지 다운로드를 위한 InputStream
        try (InputStream in = url.openStream()) {
            // 저장할 경로와 파일명 지정
            String fileName = generateFileName(imageUrl);
            Path filePath = Path.of(savePath + fileName);

            // 이미지를 파일로 저장
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName; // 생성된 파일명 반환
        }
    }
}

