package com.miraldi.warehouse.dto.converter;

import com.miraldi.warehouse.dto.orderDto.OrderItemDto;
import com.miraldi.warehouse.entities.OrderItem;
import org.mapstruct.Mapper;

@Mapper
public interface OrderItemConverter {

    OrderItemDto convertOrderItemToOrderItemDto(OrderItem orderItem);
}
