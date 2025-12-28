package com.vaabhi.store.service;


import com.vaabhi.store.model.Order;
import com.vaabhi.store.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repo;

    public OrderService(OrderRepository repo) { this.repo = repo; }

    public List<Order> getAllOrders() {
        return repo.findAll();
    }

    public List<Order> getOrdersByStatus(Order.Status status) {
        return repo.findByStatus(status);
    }

    @Transactional
    public Order updateStatus(Long id, Order.Status status) {
        Order order = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        order.setUpdatedAt(Instant.now());
        return repo.save(order);
    }

    @Transactional
    public Order cancelOrder(Long id) {
        return updateStatus(id, Order.Status.CANCELLED);
    }

    @Transactional
    public Order refundOrder(Long id) {
        return updateStatus(id, Order.Status.REFUNDED);
    }
}