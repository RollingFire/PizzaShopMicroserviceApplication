package com.austindorsey.ordermicroservice.modal;

public class OrderUpdateRequest {
    String orderStatus;

    public OrderUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus.toUpperCase();
    }

    public String getSQLUpdateStatement(String tableName, int id) {
        return "UPDATE" + tableName + " SET orderStatus='" + this.orderStatus + "' WHERE id=" + id + ";";
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus.toUpperCase();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        OrderUpdateRequest other = (OrderUpdateRequest) obj;
        if (orderStatus == null) {
            if (other.orderStatus != null)
                return false;
        } else if (!orderStatus.equals(other.orderStatus))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OrderUpdateRequest [orderStatus=" + orderStatus + "]";
    }
}