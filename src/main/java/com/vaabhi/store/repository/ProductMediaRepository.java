package com.vaabhi.store.repository;

import com.vaabhi.store.model.ProductMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductMediaRepository extends JpaRepository<ProductMedia, UUID> { }
