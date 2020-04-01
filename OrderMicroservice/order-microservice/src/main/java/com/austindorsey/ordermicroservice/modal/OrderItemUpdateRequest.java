package com.austindorsey.ordermicroservice.modal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:api.properties")
public class OrderItemUpdateRequest {
    int menuItemId;
    int quantity;
    String orderItemStatus;
    double totalCost;

    @Value("${api.host.menu}")
    private String menuHost;
    @Value("${api.port.menu}")
    private String menuPort;

    public OrderItemUpdateRequest(int menuItemId, int quantity, String orderItemStatus) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderItemStatus = orderItemStatus;
    }

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