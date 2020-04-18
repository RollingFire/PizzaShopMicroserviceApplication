package com.austindorsey.ordermicroservice.modal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class OrderItemCreateRequest {
    int orderId;
    int menuItemId;
    int quantity;
    String orderItemStatus;
    double totalCost;

    private String menuHost = "pizza-menu-api";
    private String menuPort = "9095";

    public OrderItemCreateRequest(int orderId, int menuItemId, int quantity, String orderItemStatus) throws Exception {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderItemStatus = orderItemStatus.toUpperCase();
        updateTotalCost();
    }

    public String getSQLInsertStatement(String tableName) {
        return "INSERT INTO " + tableName + " (orderId, menuItemId, quantity, orderItemStatus, cost) VALUES (" + 
                        orderId + ", " +
                        menuItemId + ", " +
                        quantity + ", '" +
                        orderItemStatus + "', " +
                        totalCost +
                        ");";
    }

    public String getSQLSelectStatement(String tableName) {
        return "SELECT * FROM " + tableName + " WHERE orderId=" + orderId + 
                                            " AND menuItemId=" + menuItemId +
                                            " AND quantity=" + quantity +
                                            " AND orderItemStatus LIKE '" + orderItemStatus +
                                           "' AND cost=" + totalCost + 
                                            "ORDER BY id DESC LIMIT 1;";
    }

    /**
     * Calls menu api to get the current cost of the menuItem and maultiplys it by the quantity
     * @return double The total cost
     * @throws Exception Throws exception if the status code of the call to the menu api is not 200
     */
    public void updateTotalCost() throws Exception {
        String url = "http://" + menuHost + ":" + menuPort + "/menuItem/" + menuItemId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) { 
            System.out.println(response.body());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> menuItem = objectMapper.readValue(response.body(), new TypeReference<Map<String,Object>>(){});
            Double perItemCost = (double) menuItem.get("cost");
            this.totalCost = perItemCost * this.quantity;
        } else {
            throw new Exception("Status code at " + url + " was " + response.statusCode());
        }
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

    public void setQuantity(int quantity) throws Exception {
        this.quantity = quantity;
        updateTotalCost();
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
        result = prime * result + ((menuHost == null) ? 0 : menuHost.hashCode());
        result = prime * result + menuItemId;
        result = prime * result + ((menuPort == null) ? 0 : menuPort.hashCode());
        result = prime * result + orderId;
        result = prime * result + ((orderItemStatus == null) ? 0 : orderItemStatus.hashCode());
        result = prime * result + quantity;
        long temp;
        temp = Double.doubleToLongBits(totalCost);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        if (menuHost == null) {
            if (other.menuHost != null)
                return false;
        } else if (!menuHost.equals(other.menuHost))
            return false;
        if (menuItemId != other.menuItemId)
            return false;
        if (menuPort == null) {
            if (other.menuPort != null)
                return false;
        } else if (!menuPort.equals(other.menuPort))
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
        if (Double.doubleToLongBits(totalCost) != Double.doubleToLongBits(other.totalCost))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OrderItemCreateRequest [menuHost=" + menuHost + ", menuItemId=" + menuItemId + ", menuPort=" + menuPort
                + ", orderId=" + orderId + ", orderItemStatus=" + orderItemStatus + ", quantity=" + quantity
                + ", totalCost=" + totalCost + "]";
    }
}