package com.vaabhi.store.controller;

import com.vaabhi.store.model.Coupon;
import com.vaabhi.store.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/marketing/coupons")
public class CouponController {
    private final CouponService service;

    public CouponController(CouponService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Coupon> create(@RequestBody @Valid Coupon coupon) {
        return ResponseEntity.ok(service.create(coupon));
    }

    @PostMapping("/apply/{code}")
    public ResponseEntity<Map<String, Object>> apply(
            @PathVariable String code, @RequestParam BigDecimal subtotal) {
        BigDecimal total = service.applyDiscount(code, subtotal);
        return ResponseEntity.ok(Map.of("code", code, "subtotal", subtotal, "total", total));
    }
}
