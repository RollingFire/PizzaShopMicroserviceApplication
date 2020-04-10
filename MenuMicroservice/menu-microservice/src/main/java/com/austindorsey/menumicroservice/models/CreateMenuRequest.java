package com.austindorsey.menumicroservice.models;

public class CreateMenuRequest {
    String menuName;
    int[] items;

    public String getName() {
        return menuName;
    }

    public void setName(String menuName) {
        this.menuName = menuName;
    }

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
    }

    public CreateMenuRequest(String menuName, int[] items) {
        this.menuName = menuName;
        this.items = items;
    }
}