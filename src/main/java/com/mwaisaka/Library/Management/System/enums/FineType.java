package com.mwaisaka.Library.Management.System.enums;

public enum FineType {
    LATE_RETURN(0.50, "Per day late"),
    DAMAGE(5.00, "Damage fee"),
    LOST(25.00, "Lost item replacement");

    private final double amount;
    private final String description;

    FineType(double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public double calculateFine(int daysOrUnits){
        return amount * daysOrUnits;
    }
}
