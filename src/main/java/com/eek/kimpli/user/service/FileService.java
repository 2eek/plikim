package com.eek.kimpli.user.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {

    // 방법 1: FileOutputStream 사용  & filePath는 경로를 포함한 이름
    public static void saveFile(byte[] content, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(content);
        }
    }

}