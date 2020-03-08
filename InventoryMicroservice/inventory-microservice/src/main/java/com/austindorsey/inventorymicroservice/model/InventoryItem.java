package com.austindorsey.inventorymicroservice.model;

public class InventoryItem {
    int id;
    String name;
    Number units;
    String unitType;
    Number restockAt;
    Number restockAmount;
    
    public InventoryItem(int id, String name, double units, String unitType, Number restockAt, Number restockAmount) {
        this.id = id;
        this.name = name;
        this.units = units;
        this.unitType = unitType;
        this.restockAt = restockAt;
        this.restockAmount = restockAmount;
    }

    public InventoryItem(InventoryItemRequest request) {
        this.id = 0;
        this.name = request.getName();
        this.units = request.getUnits();
        this.unitType = request.getUnitType();
        this.restockAt = request.getRestockAt();
        this.restockAmount = request.getRestockAmount();
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "InventoryItem [id=" + id + ", name=" + name + ", restockAmount=" + restockAmount + ", restockAt="
                + restockAt + ", unitType=" + unitType + ", units=" + units + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((restockAmount == null) ? 0 : restockAmount.hashCode());
        result = prime * result + ((restockAt == null) ? 0 : restockAt.hashCode());
        result = prime * result + ((unitType == null) ? 0 : unitType.hashCode());
        result = prime * result + ((units == null) ? 0 : units.hashCode());
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
        InventoryItem other = (InventoryItem) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (restockAmount == null) {
            if (other.restockAmount != null)
                return false;
        } else if (!restockAmount.equals(other.restockAmount))
            return false;
        if (restockAt == null) {
            if (other.restockAt != null)
                return false;
        } else if (!restockAt.equals(other.restockAt))
            return false;
        if (unitType == null) {
            if (other.unitType != null)
                return false;
        } else if (!unitType.equals(other.unitType))
            return false;
        if (units == null) {
            if (other.units != null)
                return false;
        } else if (!units.equals(other.units))
            return false;
        return true;
    }
}