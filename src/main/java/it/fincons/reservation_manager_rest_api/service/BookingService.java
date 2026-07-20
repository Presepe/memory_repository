package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.request.CreateBookingRequest;
import it.fincons.reservation_manager_rest_api.dto.response.BookingResponseDTO;
import it.fincons.reservation_manager_rest_api.exception.BookingConflictException;
import it.fincons.reservation_manager_rest_api.exception.InvalidBookingException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.mappers.BookingMapper;
import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.model.User;
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
    private final BookingMapper bookingMapper;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            UserRepository userRepository,
            BookingMapper bookingMapper
    ) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
    }

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(bookingMapper::toDto).toList();
    }

    public BookingResponseDTO getBookingById(Long id) throws ResourceNotFoundException {
        Booking booking = getBookingEntityById(id);
        return bookingMapper.toDto(booking);
    }

    public List<BookingResponseDTO> getBookingsByRoomId(Long roomId) throws ResourceNotFoundException {
        validateRoomExists(roomId);
        return bookingRepository.findByRoomId(roomId).stream().map(bookingMapper::toDto).toList();
    }

    public List<BookingResponseDTO> getBookingsByUserId(Long userId) throws ResourceNotFoundException {
        validateUserExists(userId);
        return bookingRepository.findByUserId(userId).stream().map(bookingMapper::toDto).toList();
    }

    public BookingResponseDTO createBooking(CreateBookingRequest request) throws InvalidBookingException, BookingConflictException, ResourceNotFoundException {
        validateRequest(request, null);

        Booking booking = new Booking();
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Stanza non trovata"));
        booking.setRoom(room);
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User non trovato"));
        booking.setUser(user);
        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toDto(savedBooking);
    }

    public BookingResponseDTO updateBooking(Long id, CreateBookingRequest request) throws InvalidBookingException, BookingConflictException, ResourceNotFoundException {
        // Recuperiamo l'entità usando il metodo helper privato
        Booking existingBooking = getBookingEntityById(id);

        validateRequest(request, id);

        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Stanza non trovata"));
        existingBooking.setRoom(room);
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User non trovato"));
        existingBooking.setUser(user);
        existingBooking.setDate(request.getDate());
        existingBooking.setStartTime(request.getStartTime());
        existingBooking.setEndTime(request.getEndTime());

        Booking updatedBooking = bookingRepository.save(existingBooking);

        return bookingMapper.toDto(updatedBooking);
    }

    public void deleteBooking(Long id) throws ResourceNotFoundException {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prenotazione non trovata con id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    private Booking getBookingEntityById(Long id) throws ResourceNotFoundException {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prenotazione non trovata con id: " + id));
    }

    private void validateRequest(CreateBookingRequest request, Long bookingIdToExclude) throws BookingConflictException, ResourceNotFoundException, InvalidBookingException {
        validateRoomExists(request.getRoomId());
        validateUserExists(request.getUserId());
        validateBookingTimes(request.getStartTime(), request.getEndTime());
        validateBookingConflict(bookingIdToExclude, request.getRoomId(), request.getDate(), request.getStartTime(), request.getEndTime());
    }

    private void validateRoomExists(Long roomId) throws ResourceNotFoundException {
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException("Sala non trovata con id: " + roomId);
        }
    }

    private void validateUserExists(Long userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utente non trovato con id: " + userId);
        }
    }

    private void validateBookingTimes(LocalTime startTime, LocalTime endTime) throws InvalidBookingException {
        if (!endTime.isAfter(startTime)) {
            throw new InvalidBookingException("L'orario di fine deve essere successivo all'orario di inizio");
        }
    }

    private void validateBookingConflict(Long bookingIdToExclude, Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime) throws BookingConflictException {
        List<Booking> roomBookings = bookingRepository.findByRoomId(roomId);

        boolean conflict = roomBookings.stream()
                .filter(booking -> Objects.equals(booking.getDate(), date))
                .filter(booking -> bookingIdToExclude == null || !Objects.equals(booking.getId(), bookingIdToExclude))
                .anyMatch(booking -> overlaps(startTime, endTime, booking.getStartTime(), booking.getEndTime()));

        if (conflict) {
            throw new BookingConflictException("La sala è già prenotata nell'intervallo richiesto");
        }
    }

    private boolean overlaps(LocalTime newStartTime, LocalTime newEndTime, LocalTime existingStartTime, LocalTime existingEndTime) {
        return newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime);
    }
}