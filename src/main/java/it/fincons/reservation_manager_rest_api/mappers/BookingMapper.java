package it.fincons.reservation_manager_rest_api.mappers;

import it.fincons.reservation_manager_rest_api.dto.response.BookingResponseDTO;
import it.fincons.reservation_manager_rest_api.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    // Da notare che il metodo ora è public
    public BookingResponseDTO toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingResponseDTO dto = new BookingResponseDTO();
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
