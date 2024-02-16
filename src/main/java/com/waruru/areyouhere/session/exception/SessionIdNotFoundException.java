package com.waruru.areyouhere.session.exception;

public class SessionIdNotFoundException extends RuntimeException{

        public SessionIdNotFoundException() {
            super();
        }

        public SessionIdNotFoundException(String message) {
            super(message);
        }

        public SessionIdNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
}
