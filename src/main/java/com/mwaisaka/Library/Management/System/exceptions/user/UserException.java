package com.mwaisaka.Library.Management.System.exceptions.user;

import lombok.Getter;

@Getter
public abstract class UserException extends RuntimeException {

    private final String errorCode;

    public UserException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}