package com.vaabhi.store.controller;



import com.vaabhi.store.service.FcmService;
import com.vaabhi.store.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marketing")
public class MarketingController {
    private final EmailService emailService;
    private final FcmService fcmService;

    public MarketingController(EmailService emailService, FcmService fcmService) {
        this.emailService = emailService;
        this.fcmService = fcmService;
    }

    @PostMapping("/campaign/flash-sale")
    public ResponseEntity<Void> flashSale(
            @RequestParam String title,
            @RequestParam String body,
            @RequestBody CampaignRequest req) {
        // Email blast
        emailService.sendBulk(req.emails(), title, body);
        // Push to topic "flash-sales"
        fcmService.sendToTopic("flash-sales", title, body, req.data());
        return ResponseEntity.accepted().build();
    }

    public record CampaignRequest(List<String> emails, Map<String, String> data) {}
}
