package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.CreateBookingRequest;
import it.fincons.reservation_manager_rest_api.exception.BookingConflictException;
import it.fincons.reservation_manager_rest_api.exception.InvalidBookingException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.fixture.BookingFixture;
import it.fincons.reservation_manager_rest_api.fixture.CreateBookingRequestFixture;
import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.RoomRepository;
import it.fincons.reservation_manager_rest_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock private BookingRepository bookingRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private BookingService systemUnderTest;

    @Test
    void createBooking_shouldNotCreateBooking_whenIdRoomNotExist() {
        CreateBookingRequest booking = CreateBookingRequestFixture.createValidRequest();

        when(roomRepository.existsById(booking.getRoomId())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () ->
                {
                    systemUnderTest.createBooking(booking);
                }
        );
    }
}
