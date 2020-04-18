package com.austindorsey.ordermicroservice.modal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class OrderItemUpdateRequest {
    int quantity;
    String orderItemStatus;
    double totalCost;

    private String menuHost = "pizza-menu-api";
    private String menuPort = "9095";

    public OrderItemUpdateRequest(int quantity, String orderItemStatus) {
        this.quantity = quantity;
        this.orderItemStatus = orderItemStatus.toUpperCase();
    }

    public String getSQLUpdateStatement(int id, String tableName, int menuItemId) {
        try {
            return "UPDATE " + tableName + " SET quantity=" + this.quantity
                            + ", orderItemStatus='" + this.orderItemStatus
                            + "', cost=" + getTotalCost(menuItemId)
                            + " WHERE id=" + id + ";";
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Calls menu api to get the current cost of the menuItem and maultiplys it by the quantity
     * @param menuItemId Id of the menuItem that the orderItem reprisents.
     * @return double The total cost
     * @throws Exception Throws exception if the status code of the call to the api if not 200
     */
    public double getTotalCost(int menuItemId) throws Exception {
        String url = "http://" + menuHost + ":" + menuPort + "/api/menuItem/" + menuItemId;
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
            return perItemCost * this.quantity;
        } else {
            throw new Exception("Status code at " + url + " was " + response.statusCode());
        }
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
        OrderItemUpdateRequest other = (OrderItemUpdateRequest) obj;
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
        return "OrderItemUpdateRequest [orderItemStatus=" + orderItemStatus + ", quantity=" + quantity + "]";
    }
}