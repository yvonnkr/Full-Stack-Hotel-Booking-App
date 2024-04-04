package com.yvolabs.hotelservice.controller;

import com.yvolabs.hotelservice.dto.BookingResponse;
import com.yvolabs.hotelservice.dto.RoomResponse;
import com.yvolabs.hotelservice.exception.PhotoRetrievalException;
import com.yvolabs.hotelservice.model.BookedRoom;
import com.yvolabs.hotelservice.model.Room;
import com.yvolabs.hotelservice.service.BookingService;
import com.yvolabs.hotelservice.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yvonne N
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final IRoomService roomService;
    private final BookingService bookingService;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("photo") MultipartFile photo,
                                                   @RequestParam("roomType") String roomType,
                                                   @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),
                savedRoom.getRoomPrice());
        return ResponseEntity.ok(response);

    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                if (roomResponse != null) {
                    roomResponse.setPhoto(base64Photo);
                    roomResponses.add(roomResponse);
                }
            }
        }
        return ResponseEntity.ok(roomResponses);
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        if (bookings != null) {
            List<BookingResponse> bookingInfo = bookings
                    .stream()
                    .map(booking -> new BookingResponse(booking.getBookingId(),
                            booking.getCheckInDate(),
                            booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();
            byte[] photoBytes = null;
            Blob photoBlob = room.getPhoto();
            if (photoBlob != null) {
                try {
                    photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
                } catch (SQLException e) {
                    throw new PhotoRetrievalException("Error retrieving photo");
                }
            }
            return new RoomResponse(room.getId(),
                    room.getRoomType(), room.getRoomPrice(),
                    room.isBooked(), photoBytes, bookingInfo);
        } else {
            return null;
        }
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }


}
