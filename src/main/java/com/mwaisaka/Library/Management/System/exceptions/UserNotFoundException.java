package com.mwaisaka.Library.Management.System.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message, UUID userId) {
    }
}
