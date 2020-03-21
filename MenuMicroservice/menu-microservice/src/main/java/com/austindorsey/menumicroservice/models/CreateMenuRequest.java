package com.austindorsey.menumicroservice.models;

public class CreateMenuRequest {
    String menuName;
    String items;

    public String getName() {
        return menuName;
    }

    public void setName(String menuName) {
        this.menuName = menuName;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public CreateMenuRequest(String menuName, String items) {
        this.menuName = menuName;
        this.items = items;
    }
}