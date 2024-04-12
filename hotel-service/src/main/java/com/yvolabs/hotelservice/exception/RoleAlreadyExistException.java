package com.yvolabs.hotelservice.exception;

/**
 * @author Yvonne N
 */
public class RoleAlreadyExistException extends RuntimeException {
    public RoleAlreadyExistException(String message) {
        super(message);
    }
}
