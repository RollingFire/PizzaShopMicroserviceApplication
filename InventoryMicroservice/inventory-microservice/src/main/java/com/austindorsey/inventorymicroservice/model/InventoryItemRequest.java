package com.austindorsey.inventorymicroservice.model;

public class InventoryItemRequest {
    String name;
    Number units;
    String unitType;
    Number restockAt;
    Number restockAmount;
    
    public InventoryItemRequest(String name, double units, String unitType, Number restockAt, Number restockAmount) {
        this.name = name;
        this.units = units;
        this.unitType = unitType;
        this.restockAt = restockAt;
        this.restockAmount = restockAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Number getRestockAt() {
        return restockAt;
    }

    public void setRestockAt(double restockAt) {
        this.restockAt = restockAt;
    }

    public Number getRestockAmount() {
        return restockAmount;
    }

    public void setRestockAmount(double restockAmount) {
        this.restockAmount = restockAmount;
    }

    @Override
    public String toString() {
        return "InventoryItem [name=" + name + ", restockAmount=" + restockAmount + ", restockAt="
                + restockAt + ", unitType=" + unitType + ", units=" + units + "]";
    }
}