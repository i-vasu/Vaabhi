package com.vaabhi.store.controller;


import com.vaabhi.store.service.FcmService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/marketing/push")
public class PushController {
    private final FcmService fcmService;

    public PushController(FcmService fcmService) { this.fcmService = fcmService; }

    @PostMapping("/token")
    public ResponseEntity<Void> sendToToken(
            @RequestParam String token,
            @RequestParam String title,
            @RequestParam String body,
            @RequestBody(required = false) Map<String, String> data) {
        fcmService.sendToToken(token, title, body, data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/topic/{topic}")
    public ResponseEntity<Void> sendToTopic(
            @PathVariable String topic,
            @RequestParam String title,
            @RequestParam String body,
            @RequestBody(required = false) Map<String, String> data) {
        fcmService.sendToTopic(topic, title, body, data);
        return ResponseEntity.ok().build();
    }
}
