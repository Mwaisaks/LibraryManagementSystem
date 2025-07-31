package com.mwaisaka.Library.Management.System.exceptions.user;

public class EmailAlreadyExistsException extends UserException {
    public EmailAlreadyExistsException(String message) {
        super(message, "EMAIL_ALREADY_EXISTS");
    }

}
