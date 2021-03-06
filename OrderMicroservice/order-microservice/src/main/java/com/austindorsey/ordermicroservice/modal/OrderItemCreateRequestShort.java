package com.austindorsey.ordermicroservice.modal;

public class OrderItemCreateRequestShort {
    int menuItemId;
    int quantity;
    String orderItemStatus;

    public OrderItemCreateRequestShort(int menuItemId, int quantity, String orderItemStatus) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderItemStatus = orderItemStatus.toUpperCase();
    }

    public OrderItemCreateRequest toOrderItemCreateRequest(int orderId) throws Exception {
        return new OrderItemCreateRequest(orderId, menuItemId, quantity, orderItemStatus);
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
        OrderItemCreateRequestShort other = (OrderItemCreateRequestShort) obj;
        if (menuItemId != other.menuItemId)
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
        return "OrderItemCreateRequestShort [menuItemId=" + menuItemId + ", orderItemStatus=" + orderItemStatus
                + ", quantity=" + quantity + "]";
    }
}