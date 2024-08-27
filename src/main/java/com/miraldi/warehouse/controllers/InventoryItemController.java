package com.miraldi.warehouse.controllers;

import com.miraldi.warehouse.dto.inventoryItemDto.InventoryItemDto;
import com.miraldi.warehouse.services.ServiceInventoryItem;
import com.miraldi.warehouse.utils.PageableUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static com.miraldi.warehouse.utils.PathsAndStrings.BASE_INVENTORY_ITEM_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.INVENTORY_ITEM_PATH_VARIABLE;

@RestController
@RequestMapping(BASE_INVENTORY_ITEM_PATH)
public class InventoryItemController {

    private final ServiceInventoryItem serviceInventoryItem;
    private final PageableUtil pageableUtil;

    public InventoryItemController(ServiceInventoryItem serviceInventoryItem,
                                   PageableUtil pageableUtil){
        this.serviceInventoryItem = serviceInventoryItem;
        this.pageableUtil = pageableUtil;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<InventoryItemDto> searchInventoryItems(@RequestParam(value="item-name", required = false)
                                                       String itemName,
                                                       @RequestParam(value="quantity", required = false)
                                                       Integer quantity,
                                                       @RequestParam(value="unit-price", required = false)
                                                       BigDecimal unitPrice,
                                                       @RequestParam(value = "sort-by", defaultValue = "itemName") String sortBy,
                                                       @RequestParam(value="sort-direction", defaultValue = "ASC") Sort.Direction sortDirection,
                                                       Pageable pageable) {

        var requestFilter = new ServiceInventoryItem.InventoryItemFilter();
        requestFilter.setItemName(itemName);
        requestFilter.setQuantity(quantity);
        requestFilter.setUnitPrice(unitPrice);

        Sort sort = Sort.by(new Sort.Order(sortDirection, sortBy));
        Pageable pageRequest = pageableUtil.getPageable(pageable, sort);
        return serviceInventoryItem.searchInventoryItems(requestFilter, pageRequest);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItemDto addInventoryItem(@Valid @RequestBody InventoryItemDto inventoryItemDto) {
        return serviceInventoryItem.createInventoryItem(inventoryItemDto);
    }

    @PutMapping(INVENTORY_ITEM_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    public void updateInventoryItem(@PathVariable("inventory-item-id") Long inventoryItemId,
                                    @Valid @RequestBody InventoryItemDto inventoryItemDto) {
        serviceInventoryItem.updateInventoryItem(inventoryItemId, inventoryItemDto);
    }

    @DeleteMapping(INVENTORY_ITEM_PATH_VARIABLE)
    public void deleteInventoryItem(@PathVariable("inventory-item-id") Long inventoryItemId) {
        serviceInventoryItem.deleteInventoryItem(inventoryItemId);
    }


}
