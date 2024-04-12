package com.yvolabs.hotelservice.exception;

/**
 * @author Yvonne N
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
