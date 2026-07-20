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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock private BookingRepository bookingRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private BookingService systemUnderTest;

    @Test
    void getAllBookings_shouldReturnAllBookings(){
        List<Booking> bookingList = BookingFixture.createBookingList();
        when(bookingRepository.findAll()).thenReturn(bookingList);
        assertEquals(systemUnderTest.getAllBookings().size(), bookingList.size());
    }

    @Test
    void getBookingByID_shouldReturnBooking(){
        Booking booking= BookingFixture.createValidBooking();
        when(bookingRepository.existsById(booking.getId())).thenReturn(true);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        assertEquals(booking.getId(), systemUnderTest.getBookingById(booking.getId()).getId());
    }

    @Test
    void getBookingByID_shouldThrowException_whenIDNotExist(){
        Booking booking= BookingFixture.createValidBooking();
        when(bookingRepository.existsById(booking.getId())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> systemUnderTest.getBookingById(booking.getId()));
    }

    @Test
    void getBookingsByRoomId_shouldReturnBookings_whenRoomExists() {
        Long roomId = 100L;
        List<Booking> mockBookings = BookingFixture.createBookingList();

        when(roomRepository.existsById(roomId)).thenReturn(true);
        when(bookingRepository.findByRoomId(roomId)).thenReturn(mockBookings);

        List<Booking> result = systemUnderTest.getBookingsByRoomId(roomId);

        assertEquals(mockBookings, result);
    }

    @Test
    void getBookingsByRoomId_shouldThrowException_whenRoomNotExist() {
        Long roomId = 99L;
        when(roomRepository.existsById(roomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            systemUnderTest.getBookingsByRoomId(roomId);
        });
    }

    @Test
    void getBookingsByUserId_shouldReturnBookings_whenUserExists() {
        // 1. Arrange
        Long userId = 200L;
        List<Booking> mockBookings = BookingFixture.createBookingList();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findByUserId(userId)).thenReturn(mockBookings);

        List<Booking> result = systemUnderTest.getBookingsByUserId(userId);

        assertEquals(mockBookings, result);
    }

    @Test
    void getBookingsByUserId_shouldThrowException_whenUserNotExist() {
        Long userId = 99L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            systemUnderTest.getBookingsByUserId(userId);
        });
    }

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

    @Test
    void createBooking_shouldNotCreateBooking_whenUserNotExist(){
        CreateBookingRequest bookingRequest = CreateBookingRequestFixture.createValidRequest();

        when(roomRepository.existsById(bookingRequest.getRoomId())).thenReturn(true);
        when(userRepository.existsById(bookingRequest.getUserId())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> {
            systemUnderTest.createBooking(bookingRequest);
        });
    }

    @Test
    void createBooking_shouldNotCreateBooking_whenStartTimeGreaterThenEndTime(){
        CreateBookingRequest bookingRequest = CreateBookingRequestFixture.createRequestWithInvalidTimeRange();

        when(roomRepository.existsById(bookingRequest.getRoomId())).thenReturn(true);
        when(userRepository.existsById(bookingRequest.getUserId())).thenReturn(true);
        assertThrows(InvalidBookingException.class, () -> {
            systemUnderTest.createBooking(bookingRequest);
        });
    }

    @Test
    void createBooking_shouldNotCreateBooking_whenRoomsTimesOverlaps(){
        CreateBookingRequest bookingRequest=CreateBookingRequestFixture.createValidRequest();
        Booking booking=BookingFixture.createValidBooking();

        when(roomRepository.existsById(bookingRequest.getRoomId())).thenReturn(true);
        when(userRepository.existsById(bookingRequest.getUserId())).thenReturn(true);
        when(bookingRepository.findByRoomId(bookingRequest.getRoomId())).thenReturn(List.of(booking));
        assertThrows(BookingConflictException.class, () -> {
           systemUnderTest.createBooking(bookingRequest);
        });
    }

    @Test
    void updateBooking_shouldUpdate_whenValidRequest() {
        Long bookingId = 1L;
        CreateBookingRequest request = CreateBookingRequestFixture.createValidRequest();
        Booking existingBooking = BookingFixture.createValidBooking(); // La prenotazione com'è attualmente sul DB

        // Supera validateBookingExists (chiamato dentro getBookingById)
        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        // Restituisce la prenotazione da aggiornare
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));

        // Supera validateRequest
        when(roomRepository.existsById(request.getRoomId())).thenReturn(true);
        when(userRepository.existsById(request.getUserId())).thenReturn(true);

        // Restituisce lista vuota per evitare conflitti sulla stanza
        when(bookingRepository.findByRoomId(request.getRoomId())).thenReturn(Collections.emptyList());

        // Finge il salvataggio restituendo lo stesso oggetto che gli viene passato
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = systemUnderTest.updateBooking(bookingId, request);

        assertEquals(request.getRoomId(), result.getRoomId());
        assertEquals(request.getUserId(), result.getUser());
    }

    @Test
    void updateBooking_shouldThrowException_whenBookingNotExist() {
        // 1. Arrange
        Long bookingId = 99L;
        CreateBookingRequest request = CreateBookingRequestFixture.createValidRequest();

        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        // 2. Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            systemUnderTest.updateBooking(bookingId, request);
        });
    }

    @Test
    void deleteBooking_shouldDelete_whenBookingExists() {
        Long bookingId = 1L;
        when(bookingRepository.existsById(bookingId)).thenReturn(true);

        systemUnderTest.deleteBooking(bookingId);
    }

    @Test
    void deleteBooking_shouldThrowException_whenBookingNotExist() {
        // 1. Arrange
        Long bookingId = 99L;
        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        // 2. Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            systemUnderTest.deleteBooking(bookingId);
        });
    }
}
