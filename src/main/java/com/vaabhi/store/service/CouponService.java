package com.vaabhi.store.service;

import com.vaabhi.store.model.Coupon;
import com.vaabhi.store.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class CouponService {
    private final CouponRepository repo;

    public CouponService(CouponRepository repo) { this.repo = repo; }

    @Transactional
    public Coupon create(Coupon coupon) {
        validateDates(coupon.getStartDate(), coupon.getEndDate());
        if (repo.findByCode(coupon.getCode()).isPresent()) {
            throw new IllegalArgumentException("Coupon code already exists");
        }
        return repo.save(coupon);
    }

    public BigDecimal applyDiscount(String code, BigDecimal subtotal) {
        Coupon c = repo.findByCode(code)
                .filter(this::isUsable)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired coupon"));

        BigDecimal discounted = switch (c.getDiscountType()) {
            case PERCENTAGE -> subtotal.subtract(
                    subtotal.multiply(c.getDiscountValue()).divide(BigDecimal.valueOf(100))
            );
            case FIXED -> subtotal.subtract(c.getDiscountValue());
        };
        if (discounted.compareTo(BigDecimal.ZERO) < 0) discounted = BigDecimal.ZERO;
        // increment usage
        c.setUsedCount(c.getUsedCount() + 1);
        repo.save(c);
        return discounted;
    }

    private boolean isUsable(Coupon c) {
        Instant now = Instant.now();
        boolean inWindow = now.isAfter(c.getStartDate()) && now.isBefore(c.getEndDate());
        boolean underLimit = c.getUsedCount() < c.getUsageLimit();
        return Boolean.TRUE.equals(c.getActive()) && inWindow && underLimit;
    }

    private void validateDates(Instant start, Instant end) {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new IllegalArgumentException("Invalid start/end date");
        }
    }
}
