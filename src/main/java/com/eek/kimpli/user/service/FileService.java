package com.eek.kimpli.user.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {

    // 방법 1: FileOutputStream 사용
    public static void saveFile(byte[] content, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(content);
        }
    }

    // 실제 웹 애플리케이션에서 사용하는 메서드
//    public static void saveProfileImage(MultipartFile file, String directory, String fileName) throws IOException {
//        // 서버에 저장할 경로
//        String savePath = directory + File.separator + fileName;
//
//        // 파일 정보 저장
//        byte[] fileContent = file.getBytes();
//
//        // 서버에 파일 저장
//        saveFile(fileContent, savePath);
//    }
}