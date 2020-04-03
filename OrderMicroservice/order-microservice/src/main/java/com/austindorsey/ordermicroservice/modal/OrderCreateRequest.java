package com.austindorsey.ordermicroservice.modal;

import java.util.Arrays;

public class OrderCreateRequest {
    int customerId;
    String orderStatus;
    OrderItemCreateRequestShort[] orderItems;

    public OrderCreateRequest(int customerId, String orderStatus, OrderItemCreateRequestShort[] orderItems) {
        this.customerId = customerId;
        this.orderStatus = orderStatus.toUpperCase();
        this.orderItems = orderItems;
    }

    public String getOrderSQLInsertStatement(String tableName) {
        return "INSERT INTO " + tableName + " (customerId, orderStatus) VALUES (" + 
                        customerId + ", '" +
                        orderStatus + "');";
    }

    public String getSQLSelectStatement(String tableName) {
        return "SELECT * FROM " + tableName + " WHERE customerId=" + customerId + 
                                            " AND orderStatus LIKE '" + orderStatus +
                                            "' ORDER BY id DESC LIMIT 1;";
    }

    // SELECT * FROM placedOrder WHERE customerId=1 AND orderStatus LIKE 'TEST' LIMIT 1 ORDER BY id DESC;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus.toUpperCase();
    }

    public OrderItemCreateRequestShort[] getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(OrderItemCreateRequestShort[] orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + customerId;
        result = prime * result + Arrays.hashCode(orderItems);
        result = prime * result + ((orderStatus == null) ? 0 : orderStatus.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderCreateRequest other = (OrderCreateRequest) obj;
        if (customerId != other.customerId)
            return false;
        if (!Arrays.equals(orderItems, other.orderItems))
            return false;
        if (orderStatus == null) {
            if (other.orderStatus != null)
                return false;
        } else if (!orderStatus.equals(other.orderStatus))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OrderCreateRequest [customerId=" + customerId + ", orderItems=" + Arrays.toString(orderItems)
                + ", orderStatus=" + orderStatus + "]";
    }
}