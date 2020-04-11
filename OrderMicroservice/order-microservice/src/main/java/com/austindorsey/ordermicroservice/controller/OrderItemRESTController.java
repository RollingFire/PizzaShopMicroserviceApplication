package com.austindorsey.ordermicroservice.controller;

import com.austindorsey.ordermicroservice.modal.OrderItem;
import com.austindorsey.ordermicroservice.modal.OrderItemUpdateRequest;
import com.austindorsey.ordermicroservice.services.OrderItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderItemRESTController {

    @Autowired
    private OrderItemService orderItemService;

    @RequestMapping(value = "/api/orderItem/{id:^[0-9]+$}", method = RequestMethod.GET)
    ResponseEntity<?> getOrders(@PathVariable(value = "id") int id) {
        try {
            OrderItem orders = orderItemService.getOrderItemById(id);
            if (orders != null) {
                return new ResponseEntity<>(orders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/orderItem/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> updateOrderItemById(@PathVariable(value = "id") int id,
                                @RequestBody OrderItemUpdateRequest request) {
        try {
            OrderItem orders = orderItemService.updateOrderItemById(id, request);
            if (orders != null) {
                return new ResponseEntity<>(orders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/orderItem/{id}/status", method = RequestMethod.PUT)
    ResponseEntity<?> updateOrderItemStatusById(@PathVariable(value = "id") int id,
                                @RequestBody String status) {
        try {
            OrderItem orders = orderItemService.updateOrderItemStatusById(id, status);
            if (orders != null) {
                return new ResponseEntity<>(orders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/orderItem/{orderItemStatus:(?!^\\d+$)^.+$}", method = RequestMethod.GET)
    ResponseEntity<?> getOrders(@PathVariable(value = "orderItemStatus") String status) {
        try {
            OrderItem[] orders = orderItemService.getOrderItemsByStatus(status);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}