package com.vaabhi.store.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerEmail;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private Instant createdAt = Instant.now();
    private Instant updatedAt;

    public enum Status {
        PENDING, SHIPPED, DELIVERED, RETURNED, CANCELLED, REFUNDED
    }

    // getters/setters
}
