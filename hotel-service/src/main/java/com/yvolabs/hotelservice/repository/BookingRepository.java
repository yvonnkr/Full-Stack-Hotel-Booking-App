package com.yvolabs.hotelservice.repository;

import com.yvolabs.hotelservice.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Yvonne N
 */
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

    List<BookedRoom> findByRoomId(Long roomId);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);
}
