package com.miraldi.warehouse.controllers;

import com.miraldi.warehouse.dto.orderDto.BasicOrderDto;
import com.miraldi.warehouse.dto.orderDto.OrderDto;
import com.miraldi.warehouse.dto.orderDto.OrderItemDto;
import com.miraldi.warehouse.services.ServiceOrder;
import com.miraldi.warehouse.utils.PageableUtil;
import com.miraldi.warehouse.utils.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.miraldi.warehouse.utils.PathsAndStrings.BASE_ORDER_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.CLIENT_CREATE_ORDER_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.CLIENT_SUBMIT_OR_CANCEL_ORDER_PATH_VARIABLE;
import static com.miraldi.warehouse.utils.PathsAndStrings.CLIENT_UPDATE_ORDER_PATH_VARIABLE;
import static com.miraldi.warehouse.utils.PathsAndStrings.MANAGER_APPROVE_OR_DECLINE_ORDER_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.MANAGER_FULFILL_ORDER_DELIVERY_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.MANAGER_SCHEDULE_ORDER_DELIVERY_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.SEARCH_ORDERS_PATH;
import static com.miraldi.warehouse.utils.PathsAndStrings.VIEW_ORDER_ITEMS_PATH;

@Tag(name = "Order Controller", description = "Operations related to order management")
@RestController
@RequestMapping(BASE_ORDER_PATH)
public class OrderController {

    private final ServiceOrder serviceOrder;
    private final PageableUtil pageableUtil;

    public OrderController (ServiceOrder serviceOrder,
                            PageableUtil pageableUtil){
        this.serviceOrder = serviceOrder;
        this.pageableUtil = pageableUtil;
    }

    @GetMapping(SEARCH_ORDERS_PATH)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search for orders", description = "Filter and sort orders based on different fields." +
            "They are sorted by default by the latest submitted date")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved orders")
    public Page<BasicOrderDto> searchOrders(@RequestParam(value = "orderNumber", required = false)
                                            Long orderNumber,
                                            @RequestParam(value = "status", required = false)
                                            Status status,
                                            @RequestParam(value = "sortBy", defaultValue = "submittedDate")
                                            String sortBy,
                                            @RequestParam(value="sortDirection", defaultValue = "DESC")
                                            Sort.Direction sortDirection,
                                            Pageable pageable){
        var requestFilter = new ServiceOrder.OrderRequestFilter();
        requestFilter.setOrderNumber(orderNumber);
        requestFilter.setStatus(status);

        Sort sort = Sort.by(new Sort.Order(sortDirection, sortBy));
        Pageable pageRequest = pageableUtil.getPageable(pageable, sort);
        return serviceOrder.searchOrders(requestFilter, pageRequest);
    }

    @GetMapping(VIEW_ORDER_ITEMS_PATH)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "View Order Items", description = "Retrieve the order items of an order.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved order items")
    public List<OrderItemDto> viewOrderItems(@PathVariable("order-nr") Long orderNumber){
        return serviceOrder.viewOrderItems(orderNumber);
    }

    @PostMapping(CLIENT_CREATE_ORDER_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates an order", description = "Client creates an order.")
    @ApiResponse(responseCode = "201", description = "Successfully created order")
    public BasicOrderDto createOrder(@Valid @RequestBody OrderDto orderDto){
        return serviceOrder.createOrder(orderDto);
    }

    @PutMapping(CLIENT_UPDATE_ORDER_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Updates an order", description = "Client updates an order.")
    @ApiResponse(responseCode = "200", description = "Successfully updated order")
    public BasicOrderDto clientUpdateOrder(@PathVariable("order-nr") Long orderNumber,
                            @Valid @RequestBody OrderDto orderDto){
        return serviceOrder.clientUpdatesOrder(orderNumber, orderDto);
    }

    @PutMapping(CLIENT_SUBMIT_OR_CANCEL_ORDER_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Submits or cancel an order", description = "Client submits or cancel an order.")
    @ApiResponse(responseCode = "200", description = "Successfully submitted or canceled order")
    public void clientSubmitOrCancelOrder(@PathVariable("order-nr") Long orderNumber,
                                          @RequestBody ServiceOrder.SubmitCancelRequest request){
         serviceOrder.clientSubmitsOrCancelsOrder(orderNumber, request);
    }

    @PutMapping(MANAGER_APPROVE_OR_DECLINE_ORDER_PATH)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Approves or declines an order", description = "Manager approves or declines an order.")
    @ApiResponse(responseCode = "200", description = "Successfully approved or declined order")
    public void managerApprovesOrDeclinesOrder(@PathVariable("order-nr") Long orderNumber,
                                             @RequestBody ServiceOrder.ApproveDeclineRequest approveDeclineRequest){
        serviceOrder.managerApprovesOrDeclinesOrderStatus(orderNumber, approveDeclineRequest);
    }

    @PutMapping(MANAGER_SCHEDULE_ORDER_DELIVERY_PATH)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Schedule an order", description = "Manager schedules an order.")
    @ApiResponse(responseCode = "200", description = "Successfully scheduled order")
    public void managerSchedulesOrderDelivery(@PathVariable("order-nr") Long orderNumber,
                                               @RequestBody ServiceOrder.ScheduleDeliveryRequest scheduleDeliveryRequest){
        serviceOrder.managerSchedulesOrderDelivery(orderNumber, scheduleDeliveryRequest);
    }

    @PutMapping(MANAGER_FULFILL_ORDER_DELIVERY_PATH)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fulfills an order", description = "Manager fulfills an order.")
    @ApiResponse(responseCode = "200", description = "Successfully fulfilled order")
    public void managerFulfillsOrderDelivery(@PathVariable("order-nr") Long orderNumber,
                                              @RequestBody ServiceOrder.FulfillOrderRequest fulfillOrderRequest){
        serviceOrder.managerFulfillsOrderDelivery(orderNumber, fulfillOrderRequest);
    }


}
