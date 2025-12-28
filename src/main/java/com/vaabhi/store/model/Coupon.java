package com.vaabhi.store.model;

import jakarta.persistence.*;
        import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "coupons", indexes = {
        @Index(name = "idx_coupon_code", columnList = "code", unique = true)})
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false, unique = true, length = 50)
    private String code;

    @NotNull @Enumerated(EnumType.STRING)
    private DiscountType discountType; // PERCENTAGE or FIXED

    @NotNull @DecimalMin("0.0")
    private BigDecimal discountValue;

    @NotNull private Instant startDate;
    @NotNull private Instant endDate;

    @Min(1) private Integer usageLimit = 1;
    @Min(0) private Integer usedCount = 0;

    private Boolean active = true;

    // getters/setters
    // ...
    public enum DiscountType { PERCENTAGE, FIXED }
}
