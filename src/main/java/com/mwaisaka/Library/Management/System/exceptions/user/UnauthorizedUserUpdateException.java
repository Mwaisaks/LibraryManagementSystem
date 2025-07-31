package com.mwaisaka.Library.Management.System.exceptions.user;

import lombok.Getter;

@Getter
public class UnauthorizedUserUpdateException extends UserException {
    private final Long userId;
    private final String currentUser;

    public UnauthorizedUserUpdateException(String message, Long userId, String currentUser) {
        super(message, String.format("User %s is not authorized to update user with ID: %d", currentUser, userId));
        this.userId = userId;
        this.currentUser = currentUser;


    }

}
