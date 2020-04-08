package com.austindorsey.recipemicroservice.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.austindorsey.recipemicroservice.exceptions.InventoryAPIError;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:api.properties")
public class InventoryAPIInterface {

    @Value("${api.inventory.host}")
    private String inventoryHost;
    @Value("${api.inventory.port}")
    private String inventoryPort;

    /**
     * Removes items form inventory by calling inventory API.
     * @param inventoryId   Id of the inventory item where the units will be removed.
     * @param unitsToRemove Number of units to remove from inventory.
     * @return Status code returned.
     * @throws Exception Throws when it can't make the request to the inventory API.
     */
    public int removeUnitsFromInventory(int inventoryId, double unitsToRemove) throws Exception {
        String requestBody = "{\"unitsAdded\":-" + unitsToRemove + "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + inventoryHost + ":" + inventoryPort + "/inventory/restock/" + inventoryId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new InventoryAPIError(InventoryAPIError.FailureType.ITEM_FAILED, response.toString());
        }
        return response.statusCode();
    }
}