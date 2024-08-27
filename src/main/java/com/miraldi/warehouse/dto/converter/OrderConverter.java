package com.miraldi.warehouse.dto.converter;

import com.miraldi.warehouse.dto.orderDto.BasicOrderDto;
import com.miraldi.warehouse.entities.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderConverter {

    BasicOrderDto convertOrderToBasicOrderDto(Order order);
}
