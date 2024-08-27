package com.miraldi.warehouse.dto.orderDto;

import com.miraldi.warehouse.dto.inventoryItemDto.InventoryItemDto;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OrderItemDto {
    private InventoryItemDto inventoryItem;
    @Min(1)
    private Integer requestedQuantity;
}
