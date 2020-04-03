package com.austindorsey.ordermicroservice.modal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:api.properties")
public class OrderItemCreateRequest {
    int orderId;
    int menuItemId;
    int quantity;
    String orderItemStatus;
    double totalCost;

    @Value("${api.host.menu}")
    private String menuHost;
    @Value("${api.port.menu}")
    private String menuPort;

    public OrderItemCreateRequest(int orderId, int menuItemId, int quantity, String orderItemStatus) throws Exception {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderItemStatus = orderItemStatus.toUpperCase();
    }
    
    @PostConstruct
    private void postConstruct() throws Exception {
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

    // SELECT * FROM orderItem WHERE orderId=10 AND menuItemId=1 AND quantity=1 AND orderItemStatus LIKE 'ITEM' AND cost=0.0;

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

    public double getTotalCost() {
        return totalCost;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(totalCost);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        if (Double.doubleToLongBits(totalCost) != Double.doubleToLongBits(other.totalCost))
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
        return "OrderItemCreateRequest [totalCost=" + totalCost + ", menuItemId=" + menuItemId + ", orderId=" + orderId
                + ", orderItemStatus=" + orderItemStatus + ", quantity=" + quantity + "]";
    }
}