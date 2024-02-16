package com.waruru.areyouhere.session.exception;

public class CurrentSessionNotFoundException extends RuntimeException{

        public CurrentSessionNotFoundException() {
            super();
        }

        public CurrentSessionNotFoundException(String message) {
            super(message);
        }

        public CurrentSessionNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
}
