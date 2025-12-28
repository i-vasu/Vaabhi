package com.vaabhi.store.DTO;

import com.vaabhi.store.enums.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProductResponse {
    public UUID id;
    public String name;
    public String description;
    public BigDecimal price;
    public Size size;
    public String color;
    public UUID collectionId;
    public UUID seasonId;
    public Set<String> tags;
    public Integer stockLevel;
    public String warehouseLocation;
    public List<MediaResponse> media;
}
