package com.austindorsey.ordermicroservice.services;

import java.sql.SQLException;

import com.austindorsey.ordermicroservice.modal.OrderItem;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderItemUpdateRequest;

public interface OrderItemService {
    public OrderItem[] getOrderItemByOrderId(int orderId) throws SQLException, ClassNotFoundException;
    public OrderItem addOrderItemToOrderId(int orderId, OrderItemCreateRequest request) throws SQLException, ClassNotFoundException;
    public OrderItem getOrderItemById(int id) throws SQLException, ClassNotFoundException;
    public OrderItem updateOrderItemById(int id, OrderItemUpdateRequest request) throws SQLException, ClassNotFoundException;
    public OrderItem updateOrderItemStatusById(int orderId, String status) throws SQLException, ClassNotFoundException;
    public OrderItem[] getOrderItemsByStatus(String status) throws SQLException, ClassNotFoundException;
}