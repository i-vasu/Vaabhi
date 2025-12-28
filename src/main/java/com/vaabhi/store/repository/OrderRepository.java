package com.vaabhi.store.repository;

import com.vaabhi.store.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Order.Status status);
}
