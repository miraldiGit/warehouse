package com.miraldi.warehouse.dto.inventoryItemDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateInventoryItemDto {
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
