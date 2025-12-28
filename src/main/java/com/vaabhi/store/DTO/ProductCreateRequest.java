package com.vaabhi.store.DTO;

import com.vaabhi.store.enums.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class ProductCreateRequest {
    @NotBlank
    public String name;
    public String description;
    @NotNull
    @DecimalMin("0.0") public BigDecimal price;

    //TODO: multiple-sizes for same dress
    @NotBlank public Size size;
    @NotBlank public String color;

    public UUID collectionId;
    public UUID seasonId;
    public Set<UUID> tagIds;

    @NotNull @Min(0) public Integer stockLevel = 0;
    public String warehouseLocation;
}
