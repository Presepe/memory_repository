package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.CreateBookingRequest;
import it.fincons.reservation_manager_rest_api.exception.BookingConflictException;
import it.fincons.reservation_manager_rest_api.exception.InvalidBookingException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.RoomRepository;
import it.fincons.reservation_manager_rest_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        validateBookingExists(id);

        return bookingRepository.findById(id).get();
    }

    public List<Booking> getBookingsByRoomId(Long roomId) {
        validateRoomExists(roomId);

        return bookingRepository.findByRoomId(roomId);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        validateUserExists(userId);

        return bookingRepository.findByUserId(userId);
    }

    public Booking createBooking(CreateBookingRequest request) {
        validateRequest(request, null);

        Booking booking = new Booking();

        booking.setRoomId(request.getRoomId());
        booking.setUserId(request.getUserId());
        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());

        Booking savedBooking = bookingRepository.save(booking);

        return savedBooking;
    }

    public Booking updateBooking(
            Long id,
            CreateBookingRequest request
    ) {
        Booking existingBooking = getBookingById(id);

        validateRequest(request, id);

        existingBooking.setRoomId(request.getRoomId());
        existingBooking.setUserId(request.getUserId());
        existingBooking.setDate(request.getDate());
        existingBooking.setStartTime(request.getStartTime());
        existingBooking.setEndTime(request.getEndTime());

        return bookingRepository.save(existingBooking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Prenotazione non trovata con id: " + id
            );
        }

        bookingRepository.deleteById(id);
    }

    private void validateRequest(
            CreateBookingRequest request,
            Long bookingIdToExclude
    ) {
        validateRoomExists(request.getRoomId());

        validateUserExists(request.getUserId());

        validateBookingTimes(
                request.getStartTime(),
                request.getEndTime()
        );

        validateBookingConflict(
                bookingIdToExclude,
                request.getRoomId(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        );
    }

    private void validateBookingExists(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Prenotazione non trovata con id: " + id
            );
        }
    }

    private void validateRoomExists(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException(
                    "Sala non trovata con id: " + roomId
            );
        }
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "Utente non trovato con id: " + userId
            );
        }
    }

    private void validateBookingTimes(
            LocalTime startTime,
            LocalTime endTime
    ) {
        if (!endTime.isAfter(startTime)) {
            throw new InvalidBookingException(
                    "L'orario di fine deve essere successivo "
                            + "all'orario di inizio"
            );
        }
    }

    private void validateBookingConflict(
            Long bookingIdToExclude,
            Long roomId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        List<Booking> roomBookings =
                bookingRepository.findByRoomId(roomId);

        boolean conflict = roomBookings.stream()
                .filter(booking ->
                        Objects.equals(booking.getDate(), date)
                )
                .filter(booking ->
                        bookingIdToExclude == null
                                || !Objects.equals(
                                booking.getId(),
                                bookingIdToExclude
                        )
                )
                .anyMatch(booking -> overlaps(
                        startTime,
                        endTime,
                        booking.getStartTime(),
                        booking.getEndTime()
                ));

        if (conflict) {
            throw new BookingConflictException(
                    "La sala è già prenotata nell'intervallo richiesto"
            );
        }
    }

    private boolean overlaps(
            LocalTime newStartTime,
            LocalTime newEndTime,
            LocalTime existingStartTime,
            LocalTime existingEndTime
    ) {
        return newStartTime.isBefore(existingEndTime)
                && newEndTime.isAfter(existingStartTime);
    }
}
