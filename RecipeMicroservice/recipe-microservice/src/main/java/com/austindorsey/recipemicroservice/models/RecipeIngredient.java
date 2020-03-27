package com.austindorsey.recipemicroservice.models;

public class RecipeIngredient {
    int id;
    int menuItemId;
    int inventoryItemId;
    Number quantityUsed;

    public RecipeIngredient(int id, int menuItemId, int inventoryItemId, Number quantityUsed) {
        this.id = id;
        this.menuItemId = menuItemId;
        this.inventoryItemId = inventoryItemId;
        this.quantityUsed = quantityUsed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
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
        result = prime * result + id;
        result = prime * result + inventoryItemId;
        result = prime * result + menuItemId;
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
        if (id != other.id)
            return false;
        if (inventoryItemId != other.inventoryItemId)
            return false;
        if (menuItemId != other.menuItemId)
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
        return "RecipeIngredient [id=" + id + ", inventoryItemId=" + inventoryItemId + ", menuItemId=" + menuItemId
                + ", quantityUsed=" + quantityUsed + "]";
    }
}