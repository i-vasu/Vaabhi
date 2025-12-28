package com.vaabhi.store.service;

import com.vaabhi.store.DTO.*;
import com.vaabhi.store.model.Product;
import com.vaabhi.store.model.ProductMedia;
import com.vaabhi.store.repository.ProductMediaRepository;
import com.vaabhi.store.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepo;
    private final ProductMediaRepository mediaRepo;

    public ProductService(ProductRepository productRepo,
                          ProductMediaRepository mediaRepo) {
        this.productRepo = productRepo;
        this.mediaRepo = mediaRepo;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest req) {
        Product p = new Product();
        applyProductFields(p, req);
        p = productRepo.save(p);
        return toResponse(p);
    }

    @Transactional
    public ProductResponse update(UUID id, ProductUpdateRequest req) {
        Product p = productRepo.findById(id);
        applyProductFields(p, req);
        return toResponse(p);
    }

    @Transactional
    public void delete(UUID id) { productRepo.deleteById(String.valueOf(id)); }

    @Transactional(readOnly = true)
    public ProductResponse get(UUID id) {
        Product p = productRepo.findById(String.valueOf(id)).orElseThrow(() -> new NoSuchElementException("Product not found"));
        return toResponse(p);
    }



    @Transactional
    public ProductResponse patchInventory(UUID id, InventoryPatchRequest req) {
        Product p = productRepo.findById(String.valueOf(id)).orElseThrow(() -> new NoSuchElementException("Product not found"));
        p.setStockLevel(req.stockLevel);
        p.setWarehouseLocation(req.warehouseLocation);
        return toResponse(p);
    }

    @Transactional
    public ProductResponse addMedia(UUID productId, MediaCreateRequest req) {
        Product p = productRepo.findById(productId);
        ProductMedia m = new ProductMedia();
        m.setProduct(p);
        m.setUrl(req.url);
        m.setType(ProductMedia.MediaType.valueOf(req.type));
        m.setAltText(req.altText);
        m.setPrimaryImage(Boolean.TRUE.equals(req.primaryImage));
        p.getMedia().add(m);
        productRepo.save(p);
        return toResponse(p);
    }

    @Transactional
    public ProductResponse removeMedia(UUID productId, UUID mediaId) {
        Product p = productRepo.findById(productId);
        p.getMedia().removeIf(m -> m.getId().equals(mediaId));
        mediaRepo.deleteById(mediaId);
        return toResponse(p);
    }

    private void applyProductFields(Product p, ProductCreateRequest req) {
        p.setName(req.name);
        p.setDescription(req.description);
        p.setPrice(req.price);
        p.setSize(req.size);
        p.setColor(req.color);
        p.setStockLevel(req.stockLevel);
        p.setWarehouseLocation(req.warehouseLocation);
    }


    private ProductResponse toResponse(Product p) {
        ProductResponse r = new ProductResponse();
        r.id = p.getId(); r.name = p.getName(); r.description = p.getDescription();
        r.price = p.getPrice(); r.size = p.getSize(); r.color = p.getColor();
        r.stockLevel = p.getStockLevel(); r.warehouseLocation = p.getWarehouseLocation();
        r.media = p.getMedia().stream().map(m -> {
            MediaResponse mr = new MediaResponse();
            mr.id = m.getId(); mr.url = m.getUrl(); mr.type = m.getType().name();
            mr.altText = m.getAltText(); mr.primaryImage = m.getPrimaryImage();
            return mr;
        }).collect(Collectors.toList());
        return r;
    }
}
