package com.atul.sportsmanagement.exceptions;

public class SpringSportsException extends RuntimeException {
    public SpringSportsException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public SpringSportsException(String exMessage) {
        super(exMessage);
    }
}