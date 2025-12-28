package com.vaabhi.store.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WhatsAppService {

    @Value("${whatsapp.api.url}")
    private String apiBase;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    @Value("${whatsapp.access.token}")
    private String accessToken;

    @Value("${whatsapp.broadcast.sleep.ms:100}")
    private long broadcastSleepMs;

    private final RestTemplate rest = new RestTemplate();

    // == Core sender ==
    private ResponseEntity<String> post(Object payload) {
        String url = apiBase + "/" + phoneNumberId + "/messages";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return rest.postForEntity(url, new HttpEntity<>(payload, headers), String.class);
    }

    private void ensureOk(ResponseEntity<String> resp) {
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("WhatsApp send failed: " + resp.getBody());
        }
    }

    // == 1) Plain text ==
    public void sendText(String to, String body) {
        Map<String, Object> payload = Map.of(
                "messaging_product", "whatsapp",
                "to", to,
                "type", "text",
                "text", Map.of("body", body)
        );
        ensureOk(post(payload));
    }

    // == 2) Template message ==
    // Example: name="otp_template", lang="en", components=parameters for placeholders
    public void sendTemplate(String to, String templateName, String languageCode, List<String> params) {
        List<Map<String, Object>> components = List.of(
                Map.of(
                        "type", "body",
                        "parameters", buildTextParams(params)
                )
        );

        Map<String, Object> payload = Map.of(
                "messaging_product", "whatsapp",
                "to", to,
                "type", "template",
                "template", Map.of(
                        "name", templateName,
                        "language", Map.of("code", languageCode),
                        "components", components
                )
        );
        ensureOk(post(payload));
    }

    private List<Map<String, Object>> buildTextParams(List<String> params) {
        List<Map<String, Object>> out = new ArrayList<>();
        for (String p : params) {
            out.add(Map.of(
                    "type", "text",
                    "text", p
            ));
        }
        return out;
    }

    // == 3) Media (image/document/video) ==
    // Pass a public link (or upload via media endpoint to get media_id and use id instead of link)
    public void sendImage(String to, String imageUrl, String caption) {
        Map<String, Object> payload = Map.of(
                "messaging_product", "whatsapp",
                "to", to,
                "type", "image",
                "image", caption == null
                        ? Map.of("link", imageUrl)
                        : Map.of("link", imageUrl, "caption", caption)
        );
        ensureOk(post(payload));
    }

    public void sendDocument(String to, String docUrl, String filename) {
        Map<String, Object> payload = Map.of(
                "messaging_product", "whatsapp",
                "to", to,
                "type", "document",
                "document", Map.of("link", docUrl, "filename", filename)
        );
        ensureOk(post(payload));
    }

    // == 4) Interactive buttons ==
    public void sendInteractiveButtons(String to, String bodyText, Map<String, String> buttonsIdToTitle) {
        List<Map<String, Object>> buttons = new ArrayList<>();
        buttonsIdToTitle.forEach((id, title) -> buttons.add(Map.of(
                "type", "reply",
                "reply", Map.of("id", id, "title", title)
        )));

        Map<String, Object> payload = Map.of(
                "messaging_product", "whatsapp",
                "to", to,
                "type", "interactive",
                "interactive", Map.of(
                        "type", "button",
                        "body", Map.of("text", bodyText),
                        "action", Map.of("buttons", buttons)
                )
        );
        ensureOk(post(payload));
    }

    // == 5) Broadcast (ads/marketing sale) ==
    public void broadcastTemplate(Collection<String> recipients, String templateName, String languageCode, List<String> params) {
        for (String to : recipients) {
            try {
                sendTemplate(to, templateName, languageCode, params);
                if (broadcastSleepMs > 0) Thread.sleep(broadcastSleepMs);
            } catch (Exception e) {
                // log and continue
            }
        }
    }

    public void broadcastText(Collection<String> recipients, String message) {
        for (String to : recipients) {
            try {
                sendText(to, message);
                if (broadcastSleepMs > 0) Thread.sleep(broadcastSleepMs);
            } catch (Exception e) {
                // log and continue
            }
        }
    }

    // == 6) OTP helpers ==
    // For production, store in Redis with TTL; here, simple in-memory map
    private final Map<String, String> otpStore = new HashMap<>();

    public String generateAndSendOtp(String to, String templateName, String languageCode, int digits) {
        String otp = String.format("%0" + digits + "d", new Random().nextInt((int) Math.pow(10, digits)));
        otpStore.put(to, otp);
        // Send via template with one param placeholder (e.g., "Your OTP is {{1}}")
        sendTemplate(to, templateName, languageCode, List.of(otp));
        return otp;
    }

    public boolean verifyOtp(String to, String submitted) {
        String actual = otpStore.get(to);
        boolean ok = actual != null && actual.equals(submitted);
        if (ok) otpStore.remove(to);
        return ok;
    }
}