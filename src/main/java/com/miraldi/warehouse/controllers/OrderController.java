package com.miraldi.warehouse.controllers;

import com.miraldi.warehouse.utils.PageableUtil;
import com.miraldi.warehouse.utils.Status;
import jakarta.validation.Valid;
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

import java.time.LocalDate;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final ServiceOrder serviceOrder;
    private final PageableUtil pageableUtil;

    public OrderController (ServiceOrder serviceOrder,
                            PageableUtil pageableUtil){
        this.serviceOrder = serviceOrder;
        this.pageableUtil = pageableUtil;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BasicOrderDto> searchOrders(@RequestParam(value = "order-number", required = false)
                                            Long orderNumber,
                                            @RequestParam(value = "submitted-date", required = false)
                                            LocalDate submittedDate,
                                            @RequestParam(value = "deadline-date", required = false)
                                            LocalDate deadlineDate,
                                            @RequestParam(value = "status", required = false)
                                            Status status,
                                            Pageable pageable){
        var requestFilter = new OrderRequestFilter();
        requestFilter.setOrderNumber(orderNumber);
        requestFilter.setSubmittedDate(submittedDate);
        requestFilter.setDeadlineDate(deadlineDate);
        requestFilter.setStatus(status);

        Pageable pageRequest = pageableUtil.getPageable(pageable, Sort.unsorted());
        return serviceOrder.searchOrders(requestFilter, pageRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@Valid @RequestBody CreateOrderDto createOrderDto){
        return serviceOrder.createOrder(createOrderDto);
    }

    @PutMapping("/{order-nr}")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrder(@PathVariable("order-nr") Long orderId,
                            @Valid @RequestBody UpdateOrderDto updateOrderDto){
        serviceOrder.updateOrder(orderId, updateOrderDto);
    }

    @DeleteMapping("/{order-nr}")
    public void deleteOrder(@PathVariable("order-nr") Long orderId){
        serviceOrder.deleteOrder(orderId);
    }
}
