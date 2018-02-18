package com.buzzlers.jhelpdesk.web;

public class NotAuthorizedAccessException extends RuntimeException {

    private static final long serialVersionUID = -7396029049123879745L;

    public NotAuthorizedAccessException() {
    }

    public NotAuthorizedAccessException(String message) {
        super(message);
    }
}
