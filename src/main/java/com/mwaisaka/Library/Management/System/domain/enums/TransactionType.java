package com.mwaisaka.Library.Management.System.domain.enums;

public enum TransactionType {
    BORROW("Book borrowed"),
    RETURN("Book returned");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
