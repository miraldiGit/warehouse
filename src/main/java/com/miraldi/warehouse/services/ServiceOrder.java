package com.miraldi.warehouse.services;

import com.miraldi.warehouse.dto.converter.OrderConverter;
import com.miraldi.warehouse.dto.converter.OrderItemConverter;
import com.miraldi.warehouse.dto.orderDto.BasicOrderDto;
import com.miraldi.warehouse.dto.orderDto.OrderDto;
import com.miraldi.warehouse.dto.orderDto.OrderItemDto;
import com.miraldi.warehouse.entities.InventoryItem;
import com.miraldi.warehouse.entities.Order;
import com.miraldi.warehouse.entities.OrderItem;
import com.miraldi.warehouse.entities.Truck;
import com.miraldi.warehouse.entities.TruckBookingDate;
import com.miraldi.warehouse.entities.User;
import com.miraldi.warehouse.repositories.RepositoryInventoryItem;
import com.miraldi.warehouse.repositories.RepositoryOrder;
import com.miraldi.warehouse.repositories.RepositoryOrderItem;
import com.miraldi.warehouse.repositories.RepositoryTruck;
import com.miraldi.warehouse.repositories.RepositoryUser;
import com.miraldi.warehouse.security.SecurityUtils;
import com.miraldi.warehouse.utils.Role;
import com.miraldi.warehouse.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.miraldi.warehouse.repositories.specifications.SpecificationOrder.hasOrderNumberLike;
import static com.miraldi.warehouse.repositories.specifications.SpecificationOrder.hasStatus;
import static com.miraldi.warehouse.repositories.specifications.SpecificationOrder.hasUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceOrder {

    private final RepositoryOrder repositoryOrder;
    private final RepositoryUser repositoryUser;
    private final RepositoryTruck repositoryTruck;
    private final RepositoryInventoryItem repositoryInventoryItem;
    private final OrderConverter orderConverter;
    private final OrderItemConverter orderItemConverter;
    private final RepositoryOrderItem repositoryOrderItem;

    public Page<BasicOrderDto> searchOrders(OrderRequestFilter orderRequestFilter, Pageable pageable) {

        Page<Order> pageOrder;

        Specification<Order> orderSpecification =
                Specification.where(hasOrderNumberLike(orderRequestFilter.getOrderNumber()))
                        .and(hasStatus(orderRequestFilter.getStatus()));

        if(SecurityUtils.loggedUser().getRole().equals(Role.WAREHOUSE_MANAGER)) {
            pageOrder = repositoryOrder.findAll(orderSpecification, pageable);
        }
        else{
            Specification<Order> specification = orderSpecification.and(hasUser(loggedUser()));
            pageOrder = repositoryOrder.findAll(specification, pageable);
        }

        return pageOrder.map(orderConverter::convertOrderToBasicOrderDto);
    }

    public List<OrderItemDto> viewOrderItems(Long orderNumber) {

        var order =  repositoryOrder.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order with number " +
                        orderNumber + " not found"));
        if(loggedUser().getRole().equals(Role.CLIENT)) {
            if (order.getUser().equals(loggedUser())){
                return order.getOrderItems()
                        .stream()
                        .map(orderItemConverter::convertOrderItemToOrderItemDto)
                        .toList();
            }
            else {
                throw new AccessDeniedException("Access denied: User does not have rights to view this order item");
            }
        } else if (loggedUser().getRole().equals(Role.WAREHOUSE_MANAGER)) {
                    return order.getOrderItems()
                            .stream()
                            .map(orderItemConverter::convertOrderItemToOrderItemDto)
                            .toList();
        }
        else {
                throw new AccessDeniedException("Access denied: User does not have rights to view this order item");
        }
    }

    @Transactional
    public BasicOrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        User user = loggedUser();
        order.setUser(user);
        Order savedOrder = repositoryOrder.save(order);

        if (!orderDto.getOrderItems().isEmpty()) {
            updateOrder(orderDto, savedOrder);
        }
        else {
            throw new ResourceNotFoundException("No items found");
        }
        return orderConverter.convertOrderToBasicOrderDto(order);
    }

    @Transactional
    public BasicOrderDto clientUpdatesOrder(Long orderNumber, OrderDto orderDto){
        var order = repositoryOrder.findByOrderNumber(orderNumber)
                                    .orElseThrow(() -> new ResourceNotFoundException("Order with number " +
                                            orderNumber + " not found"));
        var user = loggedUser();
        if(order.getUser().equals(user)) {
            if (order.getStatus().equals(Status.CREATED) || order.getStatus().equals(Status.DECLINED)) {
                updateOrder(orderDto, order);
            }
            else {
                throw new AccessDeniedException("Access denied: Order under the current status cannot be updated");
            }
        } else {
            throw new AccessDeniedException("Access denied: User does not have rights to update this order");
        }
        return orderConverter.convertOrderToBasicOrderDto(order);
    }

    public void clientSubmitsOrCancelsOrder(Long orderNumber, SubmitCancelRequest submitCancelRequest) {
        var order = repositoryOrder.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order with number "
                        + orderNumber + " not found"));

        var user = loggedUser();

        if(order.getUser().equals(user)) {
            if (!order.getStatus().equals(Status.FULFILLED)
                    && !order.getStatus().equals(Status.UNDER_DELIVERY)
                    && !order.getStatus().equals(Status.CANCELED)
                    && submitCancelRequest.getStatus().equals(Status.CANCELED)) {
                order.setStatus(Status.CANCELED);
                repositoryOrder.save(order);
            } else if (submitCancelRequest.getStatus().equals(Status.AWAITING_APPROVAL) &&
                    (order.getStatus().equals(Status.CREATED) || order.getStatus().equals(Status.DECLINED))) {
                order.setStatus(Status.AWAITING_APPROVAL);
                order.setSubmittedDate(LocalDate.now());
                repositoryOrder.save(order);
            }
            else {
                throw new AccessDeniedException("Access denied: Order under the current status cannot be submitted nor canceled");
            }
        }
        else {
            throw new AccessDeniedException("Access denied: User does not have rights to update this order");
        }
    }

    public void managerApprovesOrDeclinesOrderStatus(Long orderNumber, ApproveDeclineRequest approveDeclineRequest) {
        var order = repositoryOrder.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order with number "
                        + orderNumber + " not found"));

        if (loggedUser().getRole().equals(Role.WAREHOUSE_MANAGER)) {
            if (order.getStatus().equals(Status.AWAITING_APPROVAL) &&
                    approveDeclineRequest.getStatus().equals(Status.APPROVED)) {
                order.setStatus(Status.APPROVED);
                repositoryOrder.save(order);
            }
            else if (order.getStatus().equals(Status.AWAITING_APPROVAL) &&
                    approveDeclineRequest.getStatus().equals(Status.DECLINED)) {
                order.setStatus(Status.DECLINED);
                order.setDeclinedReason(approveDeclineRequest.getDeclineReason());
                repositoryOrder.save(order);
            }
            else{
                throw new AccessDeniedException("Access denied: Order under the current status cannot be approved or declined");
                }
            } else {
                throw new AccessDeniedException("Access denied: User does not have rights to update this order");
            }
        }

    @Transactional
    public void managerSchedulesOrderDelivery(Long orderNumber, ScheduleDeliveryRequest scheduleDeliveryRequest) {
        var order = repositoryOrder.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order with number "
                        + orderNumber + " not found"));

        if (loggedUser().getRole().equals(Role.WAREHOUSE_MANAGER) &&
                scheduleDeliveryRequest.getStatus().equals(Status.UNDER_DELIVERY)) {
            if(order.getStatus().equals(Status.APPROVED)) {
                int totalRequestedQuantity = order.getOrderItems().stream()
                        .mapToInt(OrderItem::getRequestedQuantity)
                        .sum();

                if(scheduleDeliveryRequest.getDeadlineDate().getDayOfWeek()== DayOfWeek.SUNDAY){
                    scheduleDeliveryRequest.setDeadlineDate(scheduleDeliveryRequest.getDeadlineDate()
                            .plusDays(1));
                    order.setDeadlineDate(scheduleDeliveryRequest.getDeadlineDate());
                }
                else{
                    order.setDeadlineDate(scheduleDeliveryRequest.getDeadlineDate());
                }
                var trucks = repositoryTruck.findAll();
                var trucksWithFreeBookingDate = trucks
                                                .stream()
                                                .filter(truck -> truck.getBookingDates().stream()
                                                .noneMatch(bookingDate ->
                                                        bookingDate.getBookingDate().equals(scheduleDeliveryRequest.getDeadlineDate())))
                                                .collect(Collectors.toSet());

                int remainingQuantity = totalRequestedQuantity;
                for (Truck truck : trucksWithFreeBookingDate) {
                        TruckBookingDate truckBookingDate = new TruckBookingDate();
                        int quantityToAllocate = Math.min(10, remainingQuantity);

                        truck.setItemsQuantityInTruck(truck.getItemsQuantityInTruck() + quantityToAllocate);
                        truckBookingDate.setBookingDate(scheduleDeliveryRequest.getDeadlineDate());
                        truck.getBookingDates().add(truckBookingDate);
                        order.getTrucks().add(truck);
                        truckBookingDate.setTruck(truck);
                        remainingQuantity -= quantityToAllocate;

                        if (remainingQuantity == 0) {
                            order.setStatus(scheduleDeliveryRequest.getStatus());
                            break;
                        }
                }
                if (remainingQuantity > 0) {
                    throw new IllegalStateException("Not enough truck capacity to fulfill the order");
                }
                Set<InventoryItem> inventoryItems = order.getOrderItems().stream()
                        .map(orderItem -> {
                            orderItem.getInventoryItem().decreaseQuantity(orderItem.getRequestedQuantity());
                            return orderItem.getInventoryItem();
                        })
                        .collect(Collectors.toSet());
                repositoryInventoryItem.saveAll(inventoryItems);
                repositoryTruck.saveAll(trucks);
                repositoryOrder.save(order);
            }
            else {
                throw new AccessDeniedException("Access denied: Order with this status " +order.getStatus()+ " cannot be scheduled for delivery");
            }
        } else {
            throw new AccessDeniedException("Access denied: User does not have rights to update this order");
        }
    }

    public void managerFulfillsOrderDelivery(Long orderNumber, FulfillOrderRequest fulfillOrderRequest){
        var order = repositoryOrder.findByOrderNumber(orderNumber)
                        .orElseThrow(() -> new ResourceNotFoundException("Order with number "
                                + orderNumber + " not found"));

        if(loggedUser().getRole().equals(Role.WAREHOUSE_MANAGER) &&
            order.getStatus().equals(Status.UNDER_DELIVERY) &&
            fulfillOrderRequest.getStatus().equals(Status.FULFILLED)){
           order.setStatus(fulfillOrderRequest.getStatus());
           repositoryOrder.save(order);
        }
        else {
            throw new AccessDeniedException("Access denied: User does not have rights to update this order or incorrect status of order");
        }
    }

    //  ┌─┐┬ ┬┌─┐┌─┐┌─┐┬─┐┌┬┐  ┌┬┐┌─┐┌┬┐┬ ┬┌─┐┌┬┐
    //  └─┐│ │├─┘├─┘│ │├┬┘ │   │││├┤  │ ├─┤│ │ ││
    //  └─┘└─┘┴  ┴  └─┘┴└─ ┴   ┴ ┴└─┘ ┴ ┴ ┴└─┘─┴┘

    private void updateOrder(OrderDto orderDto,
                             Order order) {
        Set<OrderItem> orderItemSet = new HashSet<>();
        if(order.getOrderNumber() != null) {
            repositoryOrderItem.deleteAllByOrder(order.getOrderNumber());
        }
        BigDecimal orderTotalPrice = BigDecimal.ZERO;
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            if(orderItemDto.getInventoryItem().getQuantity() < orderItemDto.getRequestedQuantity()) {
                throw new IncorrectDataException("Requested quantity for item "
                        +orderItemDto.getInventoryItem().getItemName()+
                        "exceeds available stock " +orderItemDto.getInventoryItem().getQuantity()+ ".");
            }
            else {
                 orderTotalPrice = orderTotalPrice.add(orderItemDto.getInventoryItem()
                                                  .getUnitPrice()
                                                  .multiply(BigDecimal.valueOf(orderItemDto.getRequestedQuantity())));
                orderItemConverter.convertOrderItemDtoToOrderItem(orderItemDto, orderItem);
                orderItem.setOrder(order);
                repositoryOrderItem.save(orderItem);
                orderItemSet.add(orderItem);
            }
        }
        order.setOrderTotalPrice(orderTotalPrice);
        order.setOrderItems(orderItemSet);
        repositoryOrder.save(order);
        log.info("Order with order number {} has been updated", order.getOrderNumber());
    }

    public User loggedUser(){
        return repositoryUser.findByUsernameOrEmail(SecurityUtils.loggedUser().getUsername(), SecurityUtils.loggedUser().getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApproveDeclineRequest{
        Status status;
        String declineReason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDeliveryRequest{
        Status status;
        LocalDate deadlineDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FulfillOrderRequest{
        Status status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitCancelRequest{
        Status status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderRequestFilter {

        private Long orderNumber;
        private Status status;
    }

}
