package com.vaabhi.store.controller;


import com.vaabhi.store.model.Order;
import com.vaabhi.store.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getByStatus(@PathVariable Order.Status status) {
        return ResponseEntity.ok(service.getOrdersByStatus(status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam Order.Status status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelOrder(id));
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<Order> refund(@PathVariable Long id) {
        return ResponseEntity.ok(service.refundOrder(id));
    }
}
