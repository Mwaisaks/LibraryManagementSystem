package com.mwaisaka.Library.Management.System.exceptions.user;

public class InvalidEmailFormatException extends UserException {

    public InvalidEmailFormatException(String message) {
        super(message, "INVALID_EMAIL_FORMAT");
    }

}
