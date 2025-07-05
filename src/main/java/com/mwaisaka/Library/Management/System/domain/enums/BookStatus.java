package com.mwaisaka.Library.Management.System.domain.enums;

public enum BookStatus {
    AVAILABLE("Available for checkout"),
    CHECKED_OUT("Currently checked out"),
    RESERVED("Reserved by a patron"),
    LOST("Reported as lost"),
    UNDER_MAINTENANCE("Being reported/processed");

    private final String description;

    BookStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
