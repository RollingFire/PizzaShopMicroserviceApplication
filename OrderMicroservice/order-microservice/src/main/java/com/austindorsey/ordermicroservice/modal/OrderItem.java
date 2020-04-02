package com.austindorsey.ordermicroservice.modal;

import java.sql.Date;

public class OrderItem {
    int id;
    int orderId;
    int menuItemId;
    int quantity;
    String orderItemStatus;
    Number cost;
    Date lastRevisionDate;

    public OrderItem(int id, int orderId, int menuItemId, int quantity, String orderItemStatus, Number cost,
            Date lastRevisionDate) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderItemStatus = orderItemStatus;
        this.cost = cost;
        this.lastRevisionDate = lastRevisionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderItemStatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(String orderItemStatus) {
        this.orderItemStatus = orderItemStatus;
    }

    public Number getCost() {
        return cost;
    }

    public Date getLastRevisionDate() {
        return lastRevisionDate;
    }

    public void setLastRevisionDate(Date lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cost == null) ? 0 : cost.hashCode());
        result = prime * result + id;
        result = prime * result + ((lastRevisionDate == null) ? 0 : lastRevisionDate.hashCode());
        result = prime * result + menuItemId;
        result = prime * result + orderId;
        result = prime * result + ((orderItemStatus == null) ? 0 : orderItemStatus.hashCode());
        result = prime * result + quantity;
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
        OrderItem other = (OrderItem) obj;
        if (cost == null) {
            if (other.cost != null)
                return false;
        } else if (!cost.equals(other.cost))
            return false;
        if (id != other.id)
            return false;
        if (lastRevisionDate == null) {
            if (other.lastRevisionDate != null)
                return false;
        } else if (!lastRevisionDate.equals(other.lastRevisionDate))
            return false;
        if (menuItemId != other.menuItemId)
            return false;
        if (orderId != other.orderId)
            return false;
        if (orderItemStatus == null) {
            if (other.orderItemStatus != null)
                return false;
        } else if (!orderItemStatus.equals(other.orderItemStatus))
            return false;
        if (quantity != other.quantity)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OrderItem [cost=" + cost + ", id=" + id + ", lastRevisionDate=" + lastRevisionDate + ", menuItemId="
                + menuItemId + ", orderId=" + orderId + ", orderItemStatus=" + orderItemStatus + ", quantity="
                + quantity + "]";
    }
}