package com.space.exception;

public class ResourceNotFoundException extends Exception {
    private static final long serialVersionUid = 1L;

    public ResourceNotFoundException(Object resourceId) {
        super(resourceId != null ? resourceId.toString() : null);
    }
}
