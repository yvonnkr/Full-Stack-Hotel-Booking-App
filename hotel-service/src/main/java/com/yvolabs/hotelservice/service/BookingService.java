package com.yvolabs.hotelservice.service;

import com.yvolabs.hotelservice.model.BookedRoom;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yvonne N
 */
@Service
public class BookingService implements IBookingService {
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {

        return null;
    }
}
