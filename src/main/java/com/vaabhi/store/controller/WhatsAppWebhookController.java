package com.vaabhi.store.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.Map;

@RestController
@RequestMapping("/webhook/whatsapp")
public class WhatsAppWebhookController {

    // Meta will send verification with hub.challenge (configure in Meta app)
    @GetMapping
    public ResponseEntity<String> verify(@RequestParam("hub.mode") String mode,
                                         @RequestParam("hub.verify_token") String verifyToken,
                                         @RequestParam("hub.challenge") String challenge) {
        // Compare verifyToken with your configured secret
        if ("your_verify_token".equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(403).body("Verification failed");
    }

    @PostMapping
    public ResponseEntity<Void> receive(@RequestBody Map<String, Object> payload) {
        // Handle statuses: sent, delivered, read
        // Handle messages: text, button replies (interactive->button->reply->id)
        // Persist events or trigger workflows (e.g., "track_order", "cancel_order")
        // Example: parse payload.get("entry") -> "changes" -> "value"
        // Keep parsing minimal here; implement a mapper for production.
        return ResponseEntity.ok().build();
    }
}
