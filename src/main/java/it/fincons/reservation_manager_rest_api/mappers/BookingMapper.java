package it.fincons.reservation_manager_rest_api.mappers;

import it.fincons.reservation_manager_rest_api.dto.response.BookingResponse;
import it.fincons.reservation_manager_rest_api.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    public BookingResponse toResponse(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingResponse dto = new BookingResponse();
        dto.setId(booking.getId());
        dto.setDate(booking.getDate());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());

        if (booking.getRoom() != null) {
            dto.setRoomId(booking.getRoom().getId());
            dto.setRoomName(booking.getRoom().getName());
        }

        if (booking.getUser() != null) {
            dto.setUserId(booking.getUser().getId());
            dto.setUserName(booking.getUser().getName());
        }

        return dto;
    }
}
