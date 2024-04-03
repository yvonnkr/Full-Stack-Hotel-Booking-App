package com.yvolabs.hotelservice.service;

import com.yvolabs.hotelservice.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Yvonne N
 */
public interface IRoomService {
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) throws SQLException, IOException;

    List<String> getAllRoomTypes();
}
