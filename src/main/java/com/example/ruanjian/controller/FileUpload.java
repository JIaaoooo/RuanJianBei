package com.example.ruanjian.controller;


import com.example.ruanjian.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.Map;

@CrossOrigin
@RestController
public class FileUpload {

    @Autowired
    private FileService fileService;

    /**
     * 前端nii文件传输处理
     *  由于文件传输出现一些问题，只能先通过文件名来处理文件
     * @param file nii初始图片
     * @return 返回处理后的图片的文件路径,还有原图的dicom和处理后的dicom
     */
    @PostMapping(value = "/upload")
    public Map<String, Object> uploadFile(@RequestParam String file) {
        return fileService.FileBack(file);

    }


    @PostMapping(value = "/transNiiGz",consumes = "multipart/form-dat")
    public String transNiiGz(@RequestParam("file") MultipartFile file){
        return fileService.trans(file);
    }



    /**
     * 获取图像的体积信息
     * @param file
     * @return 返回csv的文件路径
     */
    @PostMapping(value = "/diam",consumes = "multipart/form-data")
    public String GetDiam(@RequestParam("file") MultipartFile file){
        return fileService.GetDiam(file);
    }


    @PostMapping(value = "/bulk",consumes = "multipart/form-data")
    public String GetBulk(@RequestParam("file") MultipartFile file){
        return fileService.GetBulk(file);
    }


    @PostMapping("/save")
    public void saveFile(@RequestParam("file") MultipartFile file) {

    }


    @GetMapping("/test")
    public String Test(){
        System.out.println("success");
        return "success";
    }

    @PostMapping(value = "/send",consumes = "multipart/form-data")
    public ResponseEntity<InputStreamResource> sendFile(@RequestParam("file") MultipartFile file){
        FileInputStream fileInputStream = fileService.sendFile(file);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getOriginalFilename()); // 设置文件名
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 设置文件类型

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(fileInputStream));
    }

}
