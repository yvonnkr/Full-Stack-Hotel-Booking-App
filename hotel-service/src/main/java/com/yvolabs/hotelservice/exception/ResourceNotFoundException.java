package com.yvolabs.hotelservice.exception;

/**
 * @author Yvonne N
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

}
