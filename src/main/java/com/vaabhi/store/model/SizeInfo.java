package com.vaabhi.store.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SizeInfo {
    @NotNull
    @Min(0) @Column(nullable = false) private Integer stockLevel = 0;
    @DecimalMin("0.0") @Column(nullable = false) private BigDecimal price;
    private String dimensions;


    // constructors, getters, setters
}