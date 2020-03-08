package com.austindorsey.inventorymicroservice.model;

public class RestockRequest {
    Number unitsAdded;

    public RestockRequest(Number unitsAdded) {
        this.unitsAdded = unitsAdded;
    }

    public RestockRequest() {
        this.unitsAdded = null;
    }

    public Number getUnitsAdded() {
        return unitsAdded;
    }

    public void setUnitsAdded(Number unitsAdded) {
        this.unitsAdded = unitsAdded;
    }

    public void setUnitsAdded(double unitsAdded) {
        this.unitsAdded = unitsAdded;
    }

    public double getUnitsAddedDouble() {
        return unitsAdded.doubleValue();
    }

    @Override
    public String toString() {
        return "RestockRequest [unitsAdded=" + unitsAdded + "]";
    }
}