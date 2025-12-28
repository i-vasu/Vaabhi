package com.vaabhi.store.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class InventoryPatchRequest {
    @NotNull
    @Min(0) public Integer stockLevel;
    public String warehouseLocation;
}

