package com.austindorsey.ordermicroservice.controller;

import com.austindorsey.ordermicroservice.modal.Order;
import com.austindorsey.ordermicroservice.modal.OrderCreateRequest;
import com.austindorsey.ordermicroservice.modal.OrderItem;
import com.austindorsey.ordermicroservice.modal.OrderItemCreateRequestShort;
import com.austindorsey.ordermicroservice.modal.OrderUpdateRequest;
import com.austindorsey.ordermicroservice.modal.OrderWithItems;
import com.austindorsey.ordermicroservice.services.OrderItemService;
import com.austindorsey.ordermicroservice.services.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderRESTController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    ResponseEntity<?> getOrders(@RequestParam(value = "status", required = false) String status,
                                @RequestParam(value = "customerId", required = false) Integer customerId) {
        try {
            Order[] orders = orderService.getOrders(status, customerId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    ResponseEntity<?> postOrder(@RequestBody OrderCreateRequest request) {
        try {
            OrderWithItems responce = orderService.postOrder(request);
            if (responce != null) {
                    return new ResponseEntity<>(responce, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Unknown Issue, failed to create order.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getOrderById(@PathVariable(value = "id") int id) {
        try {
            Order responce = orderService.getOrderById(id);
            if (responce != null) {
                return new ResponseEntity<>(responce, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/order/{id}", method = RequestMethod.PUT)
    ResponseEntity<?> updateOrderById(@PathVariable(value = "id") int id,
                                   @RequestBody OrderUpdateRequest request) {
        try {
            Order responce = orderService.updateOrderById(id, request);
            if (responce != null) {
                return new ResponseEntity<>(responce, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/order/{id}/status", method = RequestMethod.PUT)
    ResponseEntity<?> updateOrderStatusById(@PathVariable(value = "id") int id,
                                   @RequestBody String status) {
        try {
            Order responce = orderService.updateOrderStatusById(id, status);
            if (responce != null) {
                return new ResponseEntity<>(responce, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/order/items/{orderId}", method = RequestMethod.GET)
    ResponseEntity<?> getOrderItemsByOrderId(@PathVariable(value = "orderId") int orderId) {
        try {
            OrderItem[] responce = orderItemService.getOrderItemsByOrderId(orderId);
            return new ResponseEntity<>(responce, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/order/items/{orderId}", method = RequestMethod.POST)
    ResponseEntity<?> addOrderItemToOrderId(@PathVariable(value = "orderId") int orderId,
                                            @RequestBody OrderItemCreateRequestShort request) {
        try {
            OrderItem responce = orderItemService.addOrderItemToOrderId(orderId, request.toOrderItemCreateRequest(orderId));
            return new ResponseEntity<>(responce, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}