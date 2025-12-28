package com.vaabhi.store.controller;

import com.vaabhi.store.DTO.*;
import com.vaabhi.store.model.Product;
import com.vaabhi.store.repository.ProductRepository;
import com.vaabhi.store.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import jakarta.validation.constraints.*;

import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @Autowired
    private ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id, @Valid @RequestBody ProductUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) UUID collectionId,
            @RequestParam(required = false) UUID seasonId,
            @RequestParam(required = false) Set<String> tags) {
        return ResponseEntity.ok(service.list(page, size, collectionId, seasonId, tags));
    }

    @PatchMapping("/{id}/inventory")
    public ResponseEntity<ProductResponse> patchInventory(@PathVariable UUID id,
                                                          @Valid @RequestBody InventoryPatchRequest req) {
        return ResponseEntity.ok(service.patchInventory(id, req));
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<ProductResponse> addMedia(@PathVariable UUID id,
                                                    @Valid @RequestBody MediaCreateRequest req) {
        return ResponseEntity.ok(service.addMedia(id, req));
    }

    @DeleteMapping("/{id}/media/{mediaId}")
    public ResponseEntity<ProductResponse> removeMedia(@PathVariable UUID id, @PathVariable UUID mediaId) {
        return ResponseEntity.ok(service.removeMedia(id, mediaId));
    }
}
