package com.mwaisaka.Library.Management.System.exceptions.user;

public class InvalidPasswordException extends UserException {

    public InvalidPasswordException(String message) {
        super(message, "INVALID_PASSWORD");
    }

}
