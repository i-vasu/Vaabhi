package com.vaabhi.store.service;

import com.vaabhi.store.model.ProductMedia;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    private static final Logger log = LoggerFactory.getLogger(MinioService.class);

    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public UploadResponse uploadFile(String bucketName, MultipartFile file) throws Exception {
        long startTime = System.nanoTime();

        try {
            // Ensure bucket exists
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Created bucket: {}", bucketName);
            }

            // Generate unique object name
            String objectName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // Upload file
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            log.info("Uploaded file '{}' in {} ms (size: {} bytes)", objectName, durationMs, file.getSize());

            // For consumer-facing media, return permanent public URL,
            //TODO: baseUrl() is a placeholder
            String publicUrl = String.format("%s/%s/%s",
                    "http://192.168.1.9:9000", bucketName, objectName);

            //set url
            ProductMedia.setUrl(publicUrl);
            return new UploadResponse(publicUrl, durationMs, objectName, file.getSize());

        } catch (MinioException e) {
            log.error("Error occurred: {}", e.getMessage());
            throw e;
        }
    }

    // DTO for response
    public static class UploadResponse {
        private final String url;
        private final long durationMs;
        private final String fileName;
        private final long fileSize;

        public UploadResponse(String url, long durationMs, String fileName, long fileSize) {
            this.url = url;
            this.durationMs = durationMs;
            this.fileName = fileName;
            this.fileSize = fileSize;
        }

        public String getUrl() { return url; }
        public long getDurationMs() { return durationMs; }
        public String getFileName() { return fileName; }
        public long getFileSize() { return fileSize; }
    }
}