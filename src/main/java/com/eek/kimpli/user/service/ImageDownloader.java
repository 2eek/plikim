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

    public static void imgServerSave(String imageUrl) {

        String savePath = "/home/serve/plikim_img/user_profileImg/";

        try {
            // 이미지 다운로드
            downloadImage(imageUrl, savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadImage(String imageUrl, String savePath) throws IOException {
        URL url = new URL(imageUrl);

        // 이미지 다운로드를 위한 InputStream
        try (InputStream in = url.openStream()) {
            // 저장할 경로와 파일명 지정

             // 파일명을 UUID를 이용하여 랜덤하게 생성
        String fileName = UUID.randomUUID().toString() + ".jpg";

        // 저장할 경로와 파일명 지정
        Path filePath = Path.of(savePath + fileName);

            // 이미지를 파일로 저장
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}

