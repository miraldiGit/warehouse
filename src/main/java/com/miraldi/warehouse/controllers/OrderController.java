package com.miraldi.warehouse.controllers;

import com.miraldi.warehouse.dto.orderDto.BasicOrderDto;
import com.miraldi.warehouse.dto.orderDto.OrderDto;
import com.miraldi.warehouse.dto.orderDto.OrderItemDto;
import com.miraldi.warehouse.services.ServiceOrder;
import com.miraldi.warehouse.utils.PageableUtil;
import com.miraldi.warehouse.utils.Status;
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
    public Page<BasicOrderDto> searchOrders(@RequestParam(value = "order-number", required = false)
                                            Long orderNumber,
                                            @RequestParam(value = "status", required = false)
                                            Status status,
                                            @RequestParam(value = "sort-by", defaultValue = "submittedDate")
                                            String sortBy,
                                            @RequestParam(value="sort-direction", defaultValue = "DESC")
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
    public List<OrderItemDto> viewOrderItems(Long orderNumber){
        return serviceOrder.viewOrderItems(orderNumber);
    }

    @PostMapping(CLIENT_CREATE_ORDER_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public BasicOrderDto createOrder(@Valid @RequestBody OrderDto orderDto){
        return serviceOrder.createOrder(orderDto);
    }

    @PutMapping(CLIENT_UPDATE_ORDER_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    public BasicOrderDto clientUpdateOrder(@PathVariable("order-nr") Long orderNumber,
                            @Valid @RequestBody OrderDto orderDto){
        return serviceOrder.clientUpdatesOrder(orderNumber, orderDto);
    }

    @PutMapping(CLIENT_SUBMIT_OR_CANCEL_ORDER_PATH_VARIABLE)
    @ResponseStatus(HttpStatus.OK)
    public void clientSubmitOrCancelOrder(@PathVariable("order-nr") Long orderNumber,
                                          @RequestBody ServiceOrder.SubmitCancelRequest request){
         serviceOrder.clientSubmitsOrCancelsOrder(orderNumber, request);
    }

    @PutMapping(MANAGER_APPROVE_OR_DECLINE_ORDER_PATH)
    @ResponseStatus(HttpStatus.OK)
    public void managerApprovesOrDeclinesOrder(@PathVariable("order-nr") Long orderNumber,
                                             @RequestBody ServiceOrder.ApproveDeclineRequest approveDeclineRequest){
        serviceOrder.managerApprovesOrDeclinesOrderStatus(orderNumber, approveDeclineRequest);
    }

    @PutMapping(MANAGER_SCHEDULE_ORDER_DELIVERY_PATH)
    @ResponseStatus(HttpStatus.OK)
    public void managerSchedulesOrderDelivery(@PathVariable("order-nr") Long orderNumber,
                                               @RequestBody ServiceOrder.ScheduleDeliveryRequest scheduleDeliveryRequest){
        serviceOrder.managerSchedulesOrderDelivery(orderNumber, scheduleDeliveryRequest);
    }

    @PutMapping(MANAGER_FULFILL_ORDER_DELIVERY_PATH)
    @ResponseStatus(HttpStatus.OK)
    public void managerFulfillsOrderDelivery(@PathVariable("order-nr") Long orderNumber,
                                              @RequestBody ServiceOrder.FulfillOrderRequest fulfillOrderRequest){
        serviceOrder.managerFulfillsOrderDelivery(orderNumber, fulfillOrderRequest);
    }


}
