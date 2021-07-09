package com.atul.sportsmanagement.exceptions;

import com.atul.sportsmanagement.model.Event;

public class RegisterAsAdminException extends RuntimeException {
    public RegisterAsAdminException(String message)  {
        super(message);
    }
}
