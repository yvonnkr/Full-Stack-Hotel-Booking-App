package com.yvolabs.hotelservice.exception;

/**
 * @author Yvonne N
 */
public class InvalidBookingRequestException extends RuntimeException {
    public InvalidBookingRequestException(String message) {
        super(message);
    }
}
