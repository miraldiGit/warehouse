package com.miraldi.warehouse.services;

import com.miraldi.warehouse.dto.converter.OrderConverter;
import com.miraldi.warehouse.dto.converter.OrderItemConverter;
import com.miraldi.warehouse.dto.orderDto.BasicOrderDto;
import com.miraldi.warehouse.dto.orderDto.OrderItemDto;
import com.miraldi.warehouse.entities.Order;
import com.miraldi.warehouse.entities.OrderItem;
import com.miraldi.warehouse.entities.User;
import com.miraldi.warehouse.repositories.RepositoryInventoryItem;
import com.miraldi.warehouse.repositories.RepositoryOrder;
import com.miraldi.warehouse.repositories.RepositoryOrderItem;
import com.miraldi.warehouse.repositories.RepositoryTruck;
import com.miraldi.warehouse.repositories.RepositoryUser;
import com.miraldi.warehouse.security.CustomUserDetails;
import com.miraldi.warehouse.security.SecurityUtils;
import com.miraldi.warehouse.utils.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.miraldi.warehouse.utils.Role.CLIENT;
import static com.miraldi.warehouse.utils.Role.WAREHOUSE_MANAGER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private ServiceOrder serviceOrder;
    @Mock
    private RepositoryOrder repositoryOrder;
    @Mock
    private RepositoryUser repositoryUser;
    @Mock
    private RepositoryTruck repositoryTruck;
    @Mock
    private RepositoryInventoryItem repositoryInventoryItem;
    @Captor
    private ArgumentCaptor<Order> captorOrder;

    private final OrderConverter orderConverter = Mappers.getMapper(OrderConverter.class);

    private final OrderItemConverter orderItemConverter = Mappers.getMapper(OrderItemConverter.class);

    @Mock
    private RepositoryOrderItem repositoryOrderItem;


    @BeforeEach
    void setUp() {
        serviceOrder = new ServiceOrder(repositoryOrder, repositoryUser, repositoryTruck, repositoryInventoryItem,
                orderConverter, orderItemConverter, repositoryOrderItem);
    }

    @Test
    void testSearchOrdersByStatus_WarehouseManager() {

        var orderRequestFilter = new ServiceOrder.OrderRequestFilter();
        orderRequestFilter.setStatus(Status.CREATED);

        var pageable = PageRequest.of(0, 10);

        User warehouseManager = new User();
        warehouseManager.setId(1L);
        warehouseManager.setRole(WAREHOUSE_MANAGER);
        warehouseManager.setEmail("test@test.com");

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(1L, WAREHOUSE_MANAGER,
                "username", "password", "lastName", "firstName",
                "email", null, 1000, "country", false));


        try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::loggedUser).thenReturn(customUserDetails);

            Order order1 = new Order();
            order1.setOrderNumber(1L);
            order1.setStatus(Status.CREATED);

            Order order2 = new Order();
            order2.setOrderNumber(2L);
            order2.setStatus(Status.APPROVED);

            List<Order> orders = List.of(order1, order2);

            when(repositoryOrder.findAll(any(Specification.class), eq(pageable)))
                    .thenAnswer(invocation -> {
                        invocation.getArgument(0);
                        List<Order> filteredOrders = orders.stream()
                                .filter(order -> order.getStatus() == Status.CREATED)
                                .collect(Collectors.toList());

                        return new PageImpl<>(filteredOrders, pageable, filteredOrders.size());
                    });


            BasicOrderDto basicOrderDto = orderConverter.convertOrderToBasicOrderDto(order1);

            Page<BasicOrderDto> result = serviceOrder.searchOrders(orderRequestFilter, pageable);

            verify(repositoryOrder, times(1)).findAll(any(Specification.class), eq(pageable));
            assertEquals(1, result.getTotalElements());
            assertEquals(basicOrderDto, result.getContent().getFirst());
        }
    }

    @Test
    void testViewOrderItems_WarehouseManager() {

        Long orderNumber = 1L;

        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setRole(WAREHOUSE_MANAGER);
        loggedUser.setUsername("username");
        loggedUser.setEmail("email");

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(1L, WAREHOUSE_MANAGER,
                "username", "password", "lastName", "firstName",
                "email", null, 1000, "country", false));


        try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::loggedUser).thenReturn(customUserDetails);

            Order order = new Order();
            order.setUser(loggedUser);
            OrderItem orderItem1 = new OrderItem();
            orderItem1.setId(1L);
            orderItem1.setRequestedQuantity(50);
            OrderItem orderItem2 = new OrderItem();
            orderItem2.setId(2L);
            orderItem2.setRequestedQuantity(20);
            OrderItem orderItem3 = new OrderItem();
            orderItem3.setId(3L);
            orderItem3.setRequestedQuantity(10);
            order.setOrderItems(Set.of(orderItem1, orderItem2, orderItem3));

            when(repositoryUser.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(loggedUser));
            when(repositoryOrder.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));

            List<OrderItemDto> result = serviceOrder.viewOrderItems(orderNumber);

            assertEquals(3, result.size());
        }
    }

    @Test
    void clientSubmitsOrder_Success() {

        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setRole(CLIENT);

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(1L, CLIENT,
                "username", "password", "lastName", "firstName",
                "email", null, 1000, "country", false));


        try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::loggedUser).thenReturn(customUserDetails);

            Order order = new Order();
            order.setStatus(Status.CREATED);
            order.setUser(loggedUser);

            ServiceOrder.SubmitCancelRequest submitCancelRequest = new ServiceOrder.SubmitCancelRequest();
            submitCancelRequest.setStatus(Status.AWAITING_APPROVAL);

            when(repositoryUser.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(loggedUser));
            when(repositoryOrder.findByOrderNumber(any())).thenReturn(Optional.of(order));

            serviceOrder.clientSubmitsOrCancelsOrder(any(), submitCancelRequest);

            verify(repositoryOrder, times(1)).save(captorOrder.capture());

            Order result = captorOrder.getValue();

            assertEquals(Status.AWAITING_APPROVAL, result.getStatus());
            assertEquals(LocalDate.now(), result.getSubmittedDate());
        }
    }

    @Test
    void clientCancelsOrder_Success() {

        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setRole(CLIENT);

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(1L, CLIENT,
                "username", "password", "lastName", "firstName",
                "email", null, 1000, "country", false));


        try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::loggedUser).thenReturn(customUserDetails);

            Order order = new Order();
            order.setStatus(Status.CREATED);
            order.setUser(loggedUser);

            ServiceOrder.SubmitCancelRequest submitCancelRequest = new ServiceOrder.SubmitCancelRequest();
            submitCancelRequest.setStatus(Status.CANCELED);

            when(repositoryUser.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(loggedUser));
            when(repositoryOrder.findByOrderNumber(any())).thenReturn(Optional.of(order));

            serviceOrder.clientSubmitsOrCancelsOrder(any(), submitCancelRequest);

            verify(repositoryOrder, times(1)).save(captorOrder.capture());

            Order result = captorOrder.getValue();

            assertEquals(Status.CANCELED, result.getStatus());
        }
    }

    @Test
    void clientCancelsOrder_Failed_ExceptionThrown() {

        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setRole(CLIENT);

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(1L, CLIENT,
                "username", "password", "lastName", "firstName",
                "email", null, 1000, "country", false));


        try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::loggedUser).thenReturn(customUserDetails);

            Order order = new Order();
            order.setStatus(Status.FULFILLED);
            order.setUser(loggedUser);

            ServiceOrder.SubmitCancelRequest submitCancelRequest = new ServiceOrder.SubmitCancelRequest();
            submitCancelRequest.setStatus(Status.CANCELED);

            when(repositoryUser.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(loggedUser));
            when(repositoryOrder.findByOrderNumber(any())).thenReturn(Optional.of(order));

            AccessDeniedException thrown = assertThrows(AccessDeniedException.class, () ->
                    serviceOrder.clientSubmitsOrCancelsOrder(any(), submitCancelRequest));

            assertEquals("Access denied: Order under the current status cannot be submitted nor canceled", thrown.getMessage());

        }
    }

    @Test
    void managerApproves_Success() {

        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setRole(WAREHOUSE_MANAGER);

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(1L, WAREHOUSE_MANAGER,
                "username", "password", "lastName", "firstName",
                "email", null, 1000, "country", false));


        try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::loggedUser).thenReturn(customUserDetails);

            Order order = new Order();
            order.setStatus(Status.AWAITING_APPROVAL);
            order.setUser(loggedUser);

            ServiceOrder.ApproveDeclineRequest approveDeclineRequest = new ServiceOrder.ApproveDeclineRequest();
            approveDeclineRequest.setStatus(Status.APPROVED);

            when(repositoryUser.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(loggedUser));
            when(repositoryOrder.findByOrderNumber(any())).thenReturn(Optional.of(order));

            serviceOrder.managerApprovesOrDeclinesOrderStatus(any(), approveDeclineRequest);

            verify(repositoryOrder, times(1)).save(captorOrder.capture());

            Order result = captorOrder.getValue();

            assertEquals(Status.APPROVED, result.getStatus());

        }
    }

    @Test
    void managerDeclines_Success() {

        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setRole(WAREHOUSE_MANAGER);

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(1L, WAREHOUSE_MANAGER,
                "username", "password", "lastName", "firstName",
                "email", null, 1000, "country", false));


        try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::loggedUser).thenReturn(customUserDetails);

            Order order = new Order();
            order.setStatus(Status.AWAITING_APPROVAL);
            order.setUser(loggedUser);

            ServiceOrder.ApproveDeclineRequest approveDeclineRequest = new ServiceOrder.ApproveDeclineRequest();
            approveDeclineRequest.setStatus(Status.DECLINED);
            approveDeclineRequest.setDeclineReason("Declined");

            when(repositoryUser.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(loggedUser));
            when(repositoryOrder.findByOrderNumber(any())).thenReturn(Optional.of(order));

            serviceOrder.managerApprovesOrDeclinesOrderStatus(any(), approveDeclineRequest);

            verify(repositoryOrder, times(1)).save(captorOrder.capture());

            Order result = captorOrder.getValue();

            assertEquals(Status.DECLINED, result.getStatus());
            assertEquals("Declined", result.getDeclinedReason());

        }
    }

    @Test
    void managerDeclines_Failed_ExceptionThrown() {

        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setRole(WAREHOUSE_MANAGER);

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(1L, WAREHOUSE_MANAGER,
                "username", "password", "lastName", "firstName",
                "email", null, 1000, "country", false));


        try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::loggedUser).thenReturn(customUserDetails);

            Order order = new Order();
            order.setStatus(Status.FULFILLED);
            order.setUser(loggedUser);

            ServiceOrder.ApproveDeclineRequest approveDeclineRequest = new ServiceOrder.ApproveDeclineRequest();
            approveDeclineRequest.setStatus(Status.DECLINED);
            approveDeclineRequest.setDeclineReason("Declined");

            when(repositoryUser.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(loggedUser));
            when(repositoryOrder.findByOrderNumber(any())).thenReturn(Optional.of(order));

            AccessDeniedException thrown = assertThrows(AccessDeniedException.class, () ->
                    serviceOrder.managerApprovesOrDeclinesOrderStatus(any(), approveDeclineRequest));

            assertEquals("Access denied: Order under the current status cannot be approved or declined", thrown.getMessage());

        }
    }

    @Test
    void managerFulfills_Success() {

        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setRole(WAREHOUSE_MANAGER);

        CustomUserDetails customUserDetails = new CustomUserDetails(new User(1L, WAREHOUSE_MANAGER,
                "username", "password", "lastName", "firstName",
                "email", null, 1000, "country", false));


        try (MockedStatic<SecurityUtils> mockedStatic = Mockito.mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::loggedUser).thenReturn(customUserDetails);

            Order order = new Order();
            order.setStatus(Status.UNDER_DELIVERY);
            order.setUser(loggedUser);

            ServiceOrder.FulfillOrderRequest fulfillOrderRequest = new ServiceOrder.FulfillOrderRequest();
            fulfillOrderRequest.setStatus(Status.FULFILLED);

            when(repositoryUser.findByUsernameOrEmail(any(), any())).thenReturn(Optional.of(loggedUser));
            when(repositoryOrder.findByOrderNumber(any())).thenReturn(Optional.of(order));

            serviceOrder.managerFulfillsOrderDelivery(any(), fulfillOrderRequest);

            verify(repositoryOrder, times(1)).save(captorOrder.capture());

            Order result = captorOrder.getValue();

            assertEquals(Status.FULFILLED, result.getStatus());

        }
    }


}