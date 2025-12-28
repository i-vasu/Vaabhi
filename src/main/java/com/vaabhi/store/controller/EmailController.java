package com.vaabhi.store.controller;

import com.vaabhi.store.service.EmailService;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketing/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) { this.emailService = emailService; }

    @PostMapping("/send")
    public ResponseEntity<Void> send(
            @RequestParam @Email String to,
            @RequestParam @NotBlank String subject,
            @RequestParam @NotBlank String body) {
        emailService.send(to, subject, body);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-bulk")
    public ResponseEntity<Void> sendBulk(@RequestBody BulkEmailRequest req) {
        emailService.sendBulk(req.recipients(), req.subject(), req.body());
        return ResponseEntity.ok().build();
    }

    public record BulkEmailRequest(List<@Email String> recipients, String subject, String body) {}
}
