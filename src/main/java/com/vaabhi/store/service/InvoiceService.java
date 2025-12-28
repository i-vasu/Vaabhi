package com.vaabhi.store.service;



import com.lowagie.text.*;
        import com.lowagie.text.pdf.PdfWriter;
import com.vaabhi.store.model.Order;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class InvoiceService {
    public byte[] generateInvoice(Order order) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("Invoice #" + order.getId()));
        document.add(new Paragraph("Customer: " + order.getCustomerName()));
        document.add(new Paragraph("Email: " + order.getCustomerEmail()));
        document.add(new Paragraph("Total: ₹" + order.getTotalAmount()));
        document.add(new Paragraph("Status: " + order.getStatus()));
        document.close();

        return out.toByteArray();
    }
}
