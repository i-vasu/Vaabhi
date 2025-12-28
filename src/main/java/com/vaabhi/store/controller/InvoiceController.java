package com.vaabhi.store.controller;

import com.vaabhi.store.model.Order;
import com.vaabhi.store.repository.OrderRepository;
import com.vaabhi.store.service.InvoiceService;

import org.springframework.http.*;
        import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/invoice")
public class InvoiceController {
    private final OrderRepository repo;
    private final InvoiceService invoiceService;

    public InvoiceController(OrderRepository repo, InvoiceService invoiceService) {
        this.repo = repo; this.invoiceService = invoiceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getInvoice(@PathVariable Long id) throws Exception {
        Order order = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        byte[] pdf = invoiceService.generateInvoice(order);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}