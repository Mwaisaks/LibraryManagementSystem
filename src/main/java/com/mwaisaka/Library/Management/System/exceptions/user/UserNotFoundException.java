package com.mwaisaka.Library.Management.System.exceptions.user;

public class UserNotFoundException extends UserException{
    public UserNotFoundException( String message) {
        super(message, "USER_NOT_FOUND");
    }

}
