package com.austindorsey.recipemicroservice.models;

public class RecipeIngredientRequest {
    int inventoryItemId;
    Number quantityUsed;

    public RecipeIngredientRequest(int inventoryItemId, Number quantityUsed) {
        this.inventoryItemId = inventoryItemId;
        this.quantityUsed = quantityUsed;
    }

    public int getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(int inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public Number getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(Number quantityUsed) {
        this.quantityUsed = quantityUsed;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + inventoryItemId;
        result = prime * result + ((quantityUsed == null) ? 0 : quantityUsed.hashCode());
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
        RecipeIngredient other = (RecipeIngredient) obj;
        if (inventoryItemId != other.inventoryItemId)
            return false;
        if (quantityUsed == null) {
            if (other.quantityUsed != null)
                return false;
        } else if (!quantityUsed.equals(other.quantityUsed))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RecipeIngredient [id=" + inventoryItemId
                + ", quantityUsed=" + quantityUsed + "]";
    }
}