package com.example.ruanjian.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.Map;

public interface FileService {

    Map<String, Object> FileBack(String file);
    FileInputStream sendFile(MultipartFile file);

    String trans(MultipartFile file);

    String GetDiam(MultipartFile file);

    String GetBulk(MultipartFile file);
    void FileSave(MultipartFile file);
}
