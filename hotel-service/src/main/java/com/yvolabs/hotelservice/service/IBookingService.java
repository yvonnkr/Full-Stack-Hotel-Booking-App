package com.yvolabs.hotelservice.service;

import com.yvolabs.hotelservice.model.BookedRoom;

import java.util.List;

/**
 * @author Yvonne N
 */
public interface IBookingService {
    List<BookedRoom> getAllBookings();

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    void cancelBooking(Long bookingId);
}
