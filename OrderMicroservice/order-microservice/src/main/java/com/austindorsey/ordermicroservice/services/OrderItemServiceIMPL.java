package com.austindorsey.ordermicroservice.services;

import java.sql.Connection;
import java.sql.SQLException;

import com.austindorsey.ordermicroservice.modal.OrderItem;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderItemUpdateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceIMPL implements OrderItemService {
    
    @Autowired private DriverManagerWrapper driverManagerWrapped;
    
    @Override
    public OrderItem[] getOrderItemByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderItem addOrderItemToOrderId(int orderId, OrderItemCreateRequest request)
            throws SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderItem getOrderItemById(int id) throws SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderItem updateOrderItemById(int id, OrderItemUpdateRequest request)
            throws SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderItem updateOrderItemStatusById(int orderId, String status) throws SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OrderItem[] getOrderItemsByStatus(String status) throws SQLException, ClassNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }
    
}