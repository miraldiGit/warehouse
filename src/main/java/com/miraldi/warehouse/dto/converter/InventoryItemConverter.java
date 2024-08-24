package com.miraldi.warehouse.dto.converter;

import com.miraldi.warehouse.dto.inventoryItemDto.InventoryItemDto;
import com.miraldi.warehouse.entities.InventoryItem;
import org.mapstruct.Mapper;

@Mapper
public interface InventoryItemConverter {

    InventoryItemDto convertInventoryItemToInventoryItemDto(InventoryItem inventoryItem);

    InventoryItem convertInventoryItemDtoToInventoryItem(InventoryItemDto inventoryItemDto);

}
