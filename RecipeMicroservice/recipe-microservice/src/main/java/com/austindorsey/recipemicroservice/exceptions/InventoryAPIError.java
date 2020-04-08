package com.austindorsey.recipemicroservice.exceptions;

public class InventoryAPIError extends Exception {
    private static final long serialVersionUID = 5727829845632349222L;

    public enum FailureType {
        REVERTED,
        UNREVERTED,
        ITEM_FAILED;
    }

    FailureType failureType;

    public InventoryAPIError(FailureType failureType, String message) {
        super(message);
        this.failureType = failureType;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    public void setFailureType(FailureType failureType) {
        this.failureType = failureType;
    }

    @Override
    public String toString() {
        return "InventoryAPIError [failureType=" + failureType + "]";
    }
}