package com.austindorsey.menumicroservice.models;

public class CreateMenuItemRequest {
    String catagory;
    String itemName;
    String discription;
    Double cost;

    public CreateMenuItemRequest(String catagory, String itemName, String discription, Double cost) {
        this.catagory = catagory;
        this.itemName = itemName;
        this.discription = discription;
        this.cost = cost;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getName() {
        return itemName;
    }

    public void setName(String itemName) {
        this.itemName = itemName;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}