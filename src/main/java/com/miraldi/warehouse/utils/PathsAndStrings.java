package com.miraldi.warehouse.utils;

public final class PathsAndStrings {

    private PathsAndStrings(){

    }

    //controllers
    public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
    public static final String WAREHOUSE_MANAGER = "WAREHOUSE_MANAGER";
    public static final String CLIENT = "CLIENT";
    public static final String BASE_USER_PATH = "/api/user";
    public static final String BASE_TRUCK_PATH = "/api/truck";
    public static final String BASE_INVENTORY_ITEM_PATH = "/api/inventory-item";
    public static final String BASE_ORDER_PATH = "/api/order";
    public static final String CHANGE_PASSWORD_PATH = "/change-password";
    public static final String REFRESH_TOKEN_PATH = "/refresh-token";
    public static final String TRUCK_PATH_VARIABLE = "/{truck-id}";
    public static final String INVENTORY_ITEM_PATH_VARIABLE = "/{inventory-item-id}";
    public static final String SEARCH_ORDERS_PATH = "/view-orders";
    public static final String VIEW_ORDER_ITEMS_PATH = "/view-order-items/{order-nr}";
    public static final String CLIENT_CREATE_ORDER_PATH = "/client/create-order";
    public static final String CLIENT_UPDATE_ORDER_PATH_VARIABLE = "/client/update-order/{order-nr}";
    public static final String CLIENT_SUBMIT_OR_CANCEL_ORDER_PATH_VARIABLE = "/client/submit-cancel-order/{order-nr}";
    public static final String MANAGER_APPROVE_OR_DECLINE_ORDER_PATH = "/manager/approve-decline-order/{order-nr}";
    public static final String MANAGER_SCHEDULE_ORDER_DELIVERY_PATH = "/manager/schedule-delivery/{order-nr}";
    public static final String MANAGER_FULFILL_ORDER_DELIVERY_PATH = "/manager/fulfill-delivery/{order-nr}";
    public static final String USER_PATH_VARIABLE = "/{user-id}";
    public static final String BEARER_STRING = "Bearer ";
    public static final String TOKEN_ID = "id";
    public static final String TOKEN_ROLE = "role";
    public static final String TOKEN_EMAIL = "email";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String ERROR_MESSAGE = "error_message";

    //SecurityConfig
    public static final String LOGIN_PATH = "/api/login";
    public static final String LOGOUT_PATH = "/api/logout";
    public static final String LOGIN_INCLUSIVE_PATH = "/api/login/**";
    public static final String REFRESH_TOKEN_FULL_PATH = "/api/user/refresh-token";
    public static final String REFRESH_TOKEN_INCLUSIVE_PATH = "/api/user/refresh-token/**";
    public static final String TRUCK_INCLUSIVE_PATH = "/api/truck/**";
    public static final String INVENTORY_ITEM_INCLUSIVE_PATH = "/api/inventory-item/**";
    public static final String CHANGE_PASSWORD_INCLUSIVE_PATH = "/api/user/change-password/**";
    public static final String USERS_INCLUSIVE_PATH = "/api/user/**";
    public static final String API_CLIENT_CREATE_ORDER_PATH = BASE_ORDER_PATH + CLIENT_CREATE_ORDER_PATH +"/**";
    public static final String API_VIEW_ORDER_ITEMS_PATH = BASE_ORDER_PATH + VIEW_ORDER_ITEMS_PATH +"/**";
    public static final String API_SEARCH_ORDERS_PATH = BASE_ORDER_PATH + SEARCH_ORDERS_PATH +"/**";
    public static final String API_CLIENT_UPDATE_ORDER_PATH_VARIABLE = BASE_ORDER_PATH + CLIENT_UPDATE_ORDER_PATH_VARIABLE +"/**";
    public static final String API_CLIENT_SUBMIT_OR_CANCEL_ORDER_PATH_VARIABLE = BASE_ORDER_PATH + CLIENT_SUBMIT_OR_CANCEL_ORDER_PATH_VARIABLE +"/**";
    public static final String API_MANAGER_APPROVE_OR_DECLINE_ORDER_PATH = BASE_ORDER_PATH + MANAGER_APPROVE_OR_DECLINE_ORDER_PATH +"/**";
    public static final String API_MANAGER_SCHEDULE_ORDER_DELIVERY_PATH = BASE_ORDER_PATH + MANAGER_SCHEDULE_ORDER_DELIVERY_PATH +"/**";
    public static final String API_MANAGER_FULFILL_ORDER_DELIVERY_PATH = BASE_ORDER_PATH + MANAGER_FULFILL_ORDER_DELIVERY_PATH +"/**";
}