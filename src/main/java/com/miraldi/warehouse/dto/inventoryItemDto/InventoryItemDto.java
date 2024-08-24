package com.miraldi.warehouse.dto.inventoryItemDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventoryItemDto {
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
