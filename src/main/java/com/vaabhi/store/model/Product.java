package com.vaabhi.store.model;



import com.vaabhi.store.enums.Brand;
import com.vaabhi.store.enums.Collection;
import com.vaabhi.store.enums.Season;
import com.vaabhi.store.enums.Size;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank @Column(nullable = false) private String name;
    @Column(columnDefinition = "text") private String description;
    @NotNull
    @NotBlank
    private Size size;
    @NotBlank
    private String color;
    private String colorCode;

    @NotNull
    @Min(0) @Column(nullable = false) private Integer stockLevel = 0;
    @DecimalMin("0.0") @Column(nullable = false) private BigDecimal price;
    private String dimensions;


    private Map<Size, SizeInfo> sizeAvailability = new HashMap<>();



    private String material;
    private Brand brand;
    private String careInstructions;

    private String weight;
    private Collection collection;
    private Season season;


    private String warehouseLocation;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductMedia> media = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate() { this.updatedAt = Instant.now(); }


    public void setStockLevel(@NotNull @Min(0) Integer stockLevel) {

    }
}



