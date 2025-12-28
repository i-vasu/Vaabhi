package com.vaabhi.store.service;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Service
public class FcmService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String projectId = "your-project-id"; // replace with Firebase project ID
    private final String serviceAccountPath = "src/main/resources/firebase-service-account.json";

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream(serviceAccountPath))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    // Send to a single device token
    public void sendToToken(String token, String title, String body, Map<String, String> data) throws Exception {
        sendMessage(Map.of(
                "token", token,
                "notification", Map.of("title", title, "body", body),
                "data", data == null ? Map.of() : data
        ));
    }

    // ✅ Send to a topic (e.g., "flash-sales")
    public void sendToTopic(String topic, String title, String body, Map<String, String> data) throws Exception {
        sendMessage(Map.of(
                "topic", topic,
                "notification", Map.of("title", title, "body", body),
                "data", data == null ? Map.of() : data
        ));
    }

    private void sendMessage(Map<String, Object> messagePayload) throws Exception {
        String url = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("message", messagePayload);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("FCM send failed: " + response.getBody());
        }
    }
}