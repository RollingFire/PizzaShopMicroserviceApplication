package com.austindorsey.ordermicroservice.modal;

public class OrderItemCreateRequest {
    int orderId;
    int menuItemId;
    int quantity;
    String orderItemStatus;

    public OrderItemCreateRequest(int orderId, int menuItemId, int quantity, String orderItemStatus) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderItemStatus = orderItemStatus;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        OrderItemCreateRequest other = (OrderItemCreateRequest) obj;
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
        return "OrderItemCreateRequest [menuItemId=" + menuItemId + ", orderId=" + orderId + ", orderItemStatus="
                + orderItemStatus + ", quantity=" + quantity + "]";
    }
}