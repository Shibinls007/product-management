package com.sls.product_management.dto;

import com.sls.product_management.model.Sale;

import java.time.LocalDateTime;

public class SaleDTO {
    private Long id;
    private int quantity;
    private LocalDateTime saleDate;

    public SaleDTO(Sale sale) {
        this.id = sale.getId();
        this.quantity = sale.getQuantity();
        this.saleDate = sale.getSaleDate();
    }
}