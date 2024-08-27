package com.miraldi.warehouse.controllers;

import com.miraldi.warehouse.dto.inventoryItemDto.CreateInventoryItemDto;
import com.miraldi.warehouse.dto.inventoryItemDto.InventoryItemDto;
import com.miraldi.warehouse.dto.inventoryItemDto.UpdateInventoryItemDto;
import com.miraldi.warehouse.services.ServiceInventoryItem;
import com.miraldi.warehouse.utils.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Inventory Items Controller", description = "Operations related to inventory items management")
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
    @Operation(summary = "Search for inventory items", description = "Filter and sort inventory items based on different fields." +
            "They are sorted by default by the item name")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved inventory items")
    public Page<InventoryItemDto> searchInventoryItems(@RequestParam(value="itemName", required = false)
                                                       String itemName,
                                                       @RequestParam(value="quantity", required = false)
                                                       Integer quantity,
                                                       @RequestParam(value="unitPrice", required = false)
                                                       BigDecimal unitPrice,
                                                       @RequestParam(value = "sortBy", defaultValue = "itemName") String sortBy,
                                                       @RequestParam(value="sortDirection", defaultValue = "ASC") Sort.Direction sortDirection,
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
    @Operation(summary = "Create inventory item", description = "Created an inventory item")
    @ApiResponse(responseCode = "201", description = "Successfully retrieved orders")
    public InventoryItemDto addInventoryItem(@Valid @RequestBody CreateInventoryItemDto createInventoryItemDto) {
        return serviceInventoryItem.createInventoryItem(createInventoryItemDto);
    }

    @PutMapping(INVENTORY_ITEM_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update inventory item", description = "Update an inventory item")
    @ApiResponse(responseCode = "200", description = "Successfully updated inventory item")
    public void updateInventoryItem(@PathVariable("inventory-item-id") Long inventoryItemId,
                                    @Valid @RequestBody UpdateInventoryItemDto updateInventoryItemDto) {
        serviceInventoryItem.updateInventoryItem(inventoryItemId, updateInventoryItemDto);
    }

    @DeleteMapping(INVENTORY_ITEM_PATH_VARIABLE)
    @Operation(summary = "Delete inventory item", description = "Deleted an inventory item")
    public void deleteInventoryItem(@PathVariable("inventory-item-id") Long inventoryItemId) {
        serviceInventoryItem.deleteInventoryItem(inventoryItemId);
    }


}
