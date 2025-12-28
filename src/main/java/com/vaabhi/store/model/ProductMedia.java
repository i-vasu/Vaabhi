package com.vaabhi.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "product_media")
public class ProductMedia {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank @Column(nullable = false) private String url;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private MediaType type; // IMAGE or VIDEO

    private String altText;
    private Boolean primaryImage = false;

    public static void setUrl(String publicUrl) {
    }

    public enum MediaType { IMAGE, VIDEO }
    // getters/setters
    
}
