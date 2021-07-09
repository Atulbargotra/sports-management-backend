package com.atul.sportsmanagement.exceptions;

public class NotParticipatedException extends Exception{

        public NotParticipatedException() {
        }

        public NotParticipatedException(String message) {
            super(message);
        }

        public NotParticipatedException(String message, Throwable cause) {
            super(message, cause);
        }

        public NotParticipatedException(Throwable cause) {
            super(cause);
        }

        public NotParticipatedException(String message, Throwable cause, boolean enableSuppression,
                                       boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

}
