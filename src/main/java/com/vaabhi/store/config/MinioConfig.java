package com.vaabhi.store.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://192.168.1.9:9000") // replace with your endpoint
                .credentials("minioadmin", "minioadmin")
                .build();
    }
}
