package com.eek.kimpli.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
@Component
public class ImageDownloader {

 @Value("${external.upload.path}")
private String savePath;

//    private static String generateFileName(String imageUrl) {
//        // 파일명을 UUID를 이용하여 랜덤하게 생성
//        return UUID.randomUUID().toString() + ".jpg";
//    }

  public String imgServerSave(String imageUrl) {

        System.out.println("savePath " + savePath);
    try {
        // 이미지 다운로드
    String fileName = downloadImage(imageUrl, savePath);
        // 다운로드 성공 시 fileName 반환
                    System.out.println("파일이름1 ");
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
        // URL에서 파일명 추출
String[] pathSegments = imageUrl.split("/");
String fileName = String.join("_", Arrays.copyOfRange(pathSegments, pathSegments.length - 4, pathSegments.length));
System.out.println(fileName);

        // 저장할 경로와 파일명 지정
        Path filePath = Path.of(savePath, fileName);
    System.out.println("파일이름1????? "+filePath);
        // 이미지를 파일로 저장
        Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("파일이름2 ");
        return fileName; // 생성된 파일명 반환
    }
}
}

