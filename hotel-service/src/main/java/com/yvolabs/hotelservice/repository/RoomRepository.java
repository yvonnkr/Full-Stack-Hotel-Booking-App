package com.yvolabs.hotelservice.repository;

import com.yvolabs.hotelservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yvonne N
 */
public interface RoomRepository extends JpaRepository<Room, Long> {
}
