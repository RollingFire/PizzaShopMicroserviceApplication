package com.austindorsey.ordermicroservice.modal;

import java.sql.Date;

public class Order {
    int id;
    int customerId;
    String orderStatus;
    Date datePlaced;

    public Order(int id, int customerId, String orderStatus, Date datePlaced) {
        this.id = id;
        this.customerId = customerId;
        this.orderStatus = orderStatus.toUpperCase();
        this.datePlaced = datePlaced;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Date getDatePlaced() {
        return datePlaced;
    }

    public void setDatePlaced(Date datePlaced) {
        this.datePlaced = datePlaced;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + customerId;
        result = prime * result + ((datePlaced == null) ? 0 : datePlaced.hashCode());
        result = prime * result + id;
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
        Order other = (Order) obj;
        if (customerId != other.customerId)
            return false;
        if (datePlaced == null) {
            if (other.datePlaced != null)
                return false;
        } else if (!datePlaced.equals(other.datePlaced))
            return false;
        if (id != other.id)
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
        return "Order [customerId=" + customerId + ", datePlaced=" + datePlaced + ", id=" + id + ", orderStatus="
                + orderStatus + "]";
    }
}