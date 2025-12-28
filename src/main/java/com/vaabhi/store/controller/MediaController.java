package com.vaabhi.store.controller;

import com.vaabhi.store.service.MinioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MinioService minioService;

    public MediaController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public MinioService.UploadResponse upload(@RequestParam("file") MultipartFile file) throws Exception {
        return minioService.uploadFile("vaabhi-media", file);
    }
}
