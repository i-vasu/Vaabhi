package com.vaabhi.store.repository;

import com.vaabhi.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.UUID;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByName(String name);

    Product findById(UUID productId);
}


