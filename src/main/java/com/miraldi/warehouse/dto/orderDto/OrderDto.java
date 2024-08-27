package com.miraldi.warehouse.dto.orderDto;

import lombok.Data;

import java.util.Set;

@Data
public class OrderDto {
    Set<OrderItemDto> orderItems;
}
