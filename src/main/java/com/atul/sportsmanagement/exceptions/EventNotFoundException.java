package com.atul.sportsmanagement.exceptions;

public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(String message){
        super(message);
    }
}
