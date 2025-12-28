package com.vaabhi.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vaabhi.store.service.WhatsAppService;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final WhatsAppService service;

    public WhatsAppController(WhatsAppService service) {
        this.service = service;
    }

    // == Text ==
    @PostMapping("/text")
    public ResponseEntity<Void> sendText(@RequestParam String to, @RequestParam String body) {
        service.sendText(to, body);
        return ResponseEntity.ok().build();
    }

    // == Template ==
    @PostMapping("/template")
    public ResponseEntity<Void> sendTemplate(
            @RequestParam String to,
            @RequestParam String name,
            @RequestParam(defaultValue = "en") String lang,
            @RequestBody List<String> params) {
        service.sendTemplate(to, name, lang, params);
        return ResponseEntity.ok().build();
    }

    // == Media ==
    @PostMapping("/image")
    public ResponseEntity<Void> sendImage(@RequestParam String to, @RequestParam String url, @RequestParam(required = false) String caption) {
        service.sendImage(to, url, caption);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/document")
    public ResponseEntity<Void> sendDocument(@RequestParam String to, @RequestParam String url, @RequestParam String filename) {
        service.sendDocument(to, url, filename);
        return ResponseEntity.ok().build();
    }

    // == Interactive buttons ==
    @PostMapping("/interactive/buttons")
    public ResponseEntity<Void> sendButtons(@RequestParam String to, @RequestParam String body, @RequestBody Map<String, String> idToTitle) {
        service.sendInteractiveButtons(to, body, idToTitle);
        return ResponseEntity.ok().build();
    }

    // == Broadcast ==
    @PostMapping("/broadcast/template")
    public ResponseEntity<Void> broadcastTemplate(
            @RequestBody BroadcastTemplateRequest req) {
        service.broadcastTemplate(req.recipients(), req.templateName(), req.languageCode(), req.params());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/broadcast/text")
    public ResponseEntity<Void> broadcastText(@RequestBody BroadcastTextRequest req) {
        service.broadcastText(req.recipients(), req.message());
        return ResponseEntity.accepted().build();
    }

    // == OTP ==
    @PostMapping("/otp/send")
    public ResponseEntity<Void> sendOtp(
            @RequestParam String to,
            @RequestParam String templateName,
            @RequestParam(defaultValue = "en") String lang,
            @RequestParam(defaultValue = "6") int digits) {
        service.generateAndSendOtp(to, templateName, lang, digits);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @RequestParam String to,
            @RequestParam @NotBlank String otp) {
        boolean ok = service.verifyOtp(to, otp);
        return ResponseEntity.ok(Map.of("verified", ok));
    }

    public record BroadcastTemplateRequest(List<String> recipients, String templateName, String languageCode, List<String> params) {}
    public record BroadcastTextRequest(List<String> recipients, String message) {}
}