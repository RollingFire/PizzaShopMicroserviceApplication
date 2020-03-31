package com.austindorsey.ordermicroservice.services;

import java.sql.SQLException;

import com.austindorsey.ordermicroservice.modal.Order;
import com.austindorsey.ordermicroservice.modal.OrderCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderUpdateRequest;

public interface OrderService {
    public Order[] getOrders() throws SQLException, ClassNotFoundException;
    public Order[] getOrders(int customerId) throws SQLException, ClassNotFoundException;
    public Order[] getOrders(String status) throws SQLException, ClassNotFoundException;
    public Order[] getOrders(String status, int customerId) throws SQLException, ClassNotFoundException;
    public Order postOrder(OrderCreateRequest request) throws SQLException, ClassNotFoundException;
    public Order getOrderById(int id) throws SQLException, ClassNotFoundException;
    public Order updateOrderById(OrderUpdateRequest request) throws SQLException, ClassNotFoundException;
    public Order updateOrderStatusById(String status) throws SQLException, ClassNotFoundException;
}