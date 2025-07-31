package com.mwaisaka.Library.Management.System.exceptions.user;

public class WeakPasswordException extends UserException{

    public WeakPasswordException( String message) {
        super(message, "BAD_REQUEST");
    }
}
