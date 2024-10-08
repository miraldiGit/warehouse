package com.miraldi.warehouse.services;

import com.miraldi.warehouse.dto.converter.InventoryItemConverter;
import com.miraldi.warehouse.dto.inventoryItemDto.CreateInventoryItemDto;
import com.miraldi.warehouse.dto.inventoryItemDto.InventoryItemDto;
import com.miraldi.warehouse.dto.inventoryItemDto.UpdateInventoryItemDto;
import com.miraldi.warehouse.entities.InventoryItem;
import com.miraldi.warehouse.repositories.RepositoryInventoryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.miraldi.warehouse.repositories.specifications.SpecificationInventoryItem.hasItemNameLike;
import static com.miraldi.warehouse.repositories.specifications.SpecificationInventoryItem.hasQuantity;
import static com.miraldi.warehouse.repositories.specifications.SpecificationInventoryItem.hasUnitPrice;

@Service
@RequiredArgsConstructor
public class ServiceInventoryItem {

    private final RepositoryInventoryItem repositoryInventoryItem;
    private final InventoryItemConverter inventoryItemConverter;

    public Page<InventoryItemDto> searchInventoryItems(InventoryItemFilter inventoryItemFilter, Pageable pageable) {

        Page<InventoryItem> pageInventoryItem;

        Specification<InventoryItem> inventoryItemSpecification =
               Specification.where(hasItemNameLike(inventoryItemFilter.getItemName())
                       .and(hasQuantity(inventoryItemFilter.getQuantity())
                       .and(hasUnitPrice(inventoryItemFilter.getUnitPrice()))));

        pageInventoryItem = repositoryInventoryItem.findAll(inventoryItemSpecification, pageable);

        return pageInventoryItem.map(inventoryItemConverter::convertInventoryItemToInventoryItemDto);
    }

    public InventoryItemDto createInventoryItem(CreateInventoryItemDto createInventoryItemDto){
        if(!repositoryInventoryItem.existsByItemName(createInventoryItemDto.getItemName())) {
            InventoryItem inventoryItem =
                    repositoryInventoryItem.save(
                        inventoryItemConverter.convertCreateInventoryItemDtoToInventoryItem(createInventoryItemDto)
                    );
            return inventoryItemConverter.convertInventoryItemToInventoryItemDto(inventoryItem);
        }
        else throw new IncorrectDataException("An inventory item with the same item name already exists.");
    }

    public void updateInventoryItem(Long inventoryItemId, UpdateInventoryItemDto updateInventoryItemDto){
        var inventoryItem = repositoryInventoryItem.findById(inventoryItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item with id: "
                        +inventoryItemId+ " not found"));

        if(updateInventoryItemDto.getItemName() != null &&
           updateInventoryItemDto.getQuantity() != null &&
           updateInventoryItemDto.getUnitPrice() != null &&
           updateInventoryItemDto.getQuantity() >= 0 &&
           updateInventoryItemDto.getUnitPrice().compareTo(BigDecimal.ZERO) > 0){
            inventoryItemConverter.convertUpdateInventoryItemDtoToInventoryItem(updateInventoryItemDto, inventoryItem);
            repositoryInventoryItem.save(inventoryItem);
        }
        else throw new IncorrectDataException("An inventory item must not have fields as null and/or " +
                "quantity and/or unit price as negative numbers.");
    }

    public void deleteInventoryItem(Long inventoryItemId){
        var inventoryItem = repositoryInventoryItem.findById(inventoryItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item with id: "
                        +inventoryItemId+ " not found"));
        inventoryItem.setDeleted(true);
        repositoryInventoryItem.save(inventoryItem);
    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryItemFilter {

        private String itemName;
        private Integer quantity;
        private BigDecimal unitPrice;
    }

}
