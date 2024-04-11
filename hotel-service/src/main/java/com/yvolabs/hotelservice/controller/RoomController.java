package com.yvolabs.hotelservice.controller;

import com.yvolabs.hotelservice.dto.BookingResponse;
import com.yvolabs.hotelservice.dto.RoomResponse;
import com.yvolabs.hotelservice.exception.PhotoRetrievalException;
import com.yvolabs.hotelservice.exception.ResourceNotFoundException;
import com.yvolabs.hotelservice.model.BookedRoom;
import com.yvolabs.hotelservice.model.Room;
import com.yvolabs.hotelservice.service.BookingService;
import com.yvolabs.hotelservice.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return ResponseEntity.ok(getRoomResponses(rooms));
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {

        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() :
                roomService.getRoomPhotoByRoomId(roomId);

        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ?
                new SerialBlob(photoBytes) :
                null;
        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        theRoom.setPhoto(photoBlob);

        RoomResponse roomResponse = getRoomResponse(theRoom);
        return ResponseEntity.ok(roomResponse);

    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
        Optional<Room> roomOpt = roomService.getRoomById(roomId);
        return roomOpt.map((room) -> {
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));

        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam("roomType") String roomType) throws SQLException {

        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);

        List<RoomResponse> roomResponses = getRoomResponses(availableRooms);
        return roomResponses.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(roomResponses);
    }

    private List<RoomResponse> getRoomResponses(List<Room> availableRooms) throws SQLException {
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : availableRooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add(roomResponse);
            }
        }
        return roomResponses;
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());

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

    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }


}
