package it.fincons.reservation_manager_rest_api.mapper;

import it.fincons.reservation_manager_rest_api.dto.CreateBookingRequest;
import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.model.User;
import it.fincons.reservation_manager_rest_api.repository.RoomRepository;
import it.fincons.reservation_manager_rest_api.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    private static RoomRepository roomRepository;
    private static UserRepository userRepository;

    public static Booking toEntity(CreateBookingRequest request) {

        Room room = roomRepository.findById(request.getRoomId()).orElse(null);
        User user = userRepository.findById(request.getUserId()).orElse(null);

        return Booking.builder()
                .room(room)
                .user(user)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
    }
}