package com.austindorsey.ordermicroservice.services;

import java.sql.SQLException;

import com.austindorsey.ordermicroservice.modal.Order;
import com.austindorsey.ordermicroservice.modal.OrderCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderUpdateRequest;
import com.austindorsey.ordermicroservice.modal.OrderWithItems;

public interface OrderService {
    public Order[] getOrders(String status, Integer customerId) throws SQLException, ClassNotFoundException;
    public OrderWithItems postOrder(OrderCreateRequest request) throws SQLException, ClassNotFoundException;
    public Order getOrderById(int id) throws SQLException, ClassNotFoundException;
    public Order updateOrderById(int id, OrderUpdateRequest request) throws SQLException, ClassNotFoundException;
    public Order updateOrderStatusById(int id, String status) throws SQLException, ClassNotFoundException;
}