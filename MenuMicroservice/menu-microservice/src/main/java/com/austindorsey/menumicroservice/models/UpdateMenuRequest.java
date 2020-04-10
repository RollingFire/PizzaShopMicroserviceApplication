package com.austindorsey.menumicroservice.models;

public class UpdateMenuRequest {
    int[] items;

    public UpdateMenuRequest(int[] items) {
        this.items = items;
    }
    
    public UpdateMenuRequest() {
        this.items = null;
    }

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
    }
}