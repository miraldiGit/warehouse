package com.miraldi.warehouse.dto.converter;

import com.miraldi.warehouse.dto.inventoryItemDto.CreateInventoryItemDto;
import com.miraldi.warehouse.dto.inventoryItemDto.InventoryItemDto;
import com.miraldi.warehouse.dto.inventoryItemDto.UpdateInventoryItemDto;
import com.miraldi.warehouse.entities.InventoryItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface InventoryItemConverter {

    InventoryItemDto convertInventoryItemToInventoryItemDto(InventoryItem inventoryItem);

    InventoryItem convertInventoryItemDtoToInventoryItem(InventoryItemDto inventoryItemDto);

    InventoryItem convertCreateInventoryItemDtoToInventoryItem(CreateInventoryItemDto inventoryItemDto);

    void convertUpdateInventoryItemDtoToInventoryItem(UpdateInventoryItemDto updateInventoryItemDto,
                                                               @MappingTarget InventoryItem inventoryItem);

}
