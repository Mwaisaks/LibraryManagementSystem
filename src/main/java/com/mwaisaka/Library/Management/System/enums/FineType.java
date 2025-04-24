package com.mwaisaka.Library.Management.System.enums;

import java.math.BigDecimal;

public enum FineType {
    LATE_RETURN(0.50, "Per day late"),
    DAMAGE(5.00, "Damage fee"),
    LOST(25.00, "Lost item replacement");

    private final BigDecimal amount;
    private final String description;

    FineType(double amount, String description) {
        this.amount = BigDecimal.valueOf(amount);
        this.description = description;
    }

    public BigDecimal calculateFine(int daysOrUnits){
        return amount.multiply(BigDecimal.valueOf(daysOrUnits));
    }

    public String getDisplayText(){
        return description + ":$" + amount + (this == LATE_RETURN ? "/day" : "");
    }
}
