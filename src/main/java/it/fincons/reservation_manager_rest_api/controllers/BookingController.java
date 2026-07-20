package it.fincons.reservation_manager_rest_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.fincons.reservation_manager_rest_api.dto.request.CreateBookingRequest;
import it.fincons.reservation_manager_rest_api.dto.response.BookingResponseDTO;
import it.fincons.reservation_manager_rest_api.exception.BookingConflictException;
import it.fincons.reservation_manager_rest_api.exception.InvalidBookingException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @Operation(summary = "Find all bookings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings found"),
            @ApiResponse(responseCode = "404", description = "Bookings not found")
    })
    @GetMapping("/bookings")
    public List<BookingResponseDTO> getAllBookings() {
        return service.getAllBookings();
    }

    @Operation(summary = "Find booking by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking found"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/bookings/{id}")
    public BookingResponseDTO getBookingById(@PathVariable Long id) throws ResourceNotFoundException {
        return service.getBookingById(id);
    }

    @Operation(summary = "Create new booking")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Booking created"),
            @ApiResponse(responseCode = "400", description = "Booking not created")
    })
    @PostMapping("/bookings")
    public BookingResponseDTO createBooking(@Valid @RequestBody CreateBookingRequest request) throws InvalidBookingException, BookingConflictException, ResourceNotFoundException {
        return service.createBooking(request);
    }

    @Operation(summary = "Update a booking by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking updated"),
            @ApiResponse(responseCode = "400", description = "Booking not updated")
    })
    @PutMapping("/bookings/{id}")
    public BookingResponseDTO updateBooking(@PathVariable Long id, @Valid @RequestBody CreateBookingRequest updatedBooking) throws InvalidBookingException, BookingConflictException, ResourceNotFoundException {
        return service.updateBooking(id, updatedBooking);
    }

    @Operation(summary = "Delete a booking by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking deleted"),
            @ApiResponse(responseCode = "400", description = "Booking not deleted")
    })
    @DeleteMapping("/bookings/{id}")
    public void deleteBooking(@PathVariable Long id) throws ResourceNotFoundException {
        service.deleteBooking(id);
    }

    @Operation(summary = "Find all bookings by room ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings found"),
            @ApiResponse(responseCode = "404", description = "Bookings not found")
    })
    @GetMapping("/rooms/{roomId}/bookings")
    public List<BookingResponseDTO> getBookingByRoomId(@PathVariable Long roomId) throws ResourceNotFoundException {
        return service.getBookingsByRoomId(roomId);
    }

    @Operation(summary = "Find all bookings by user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings found"),
            @ApiResponse(responseCode = "404", description = "Bookings not found")
    })
    @GetMapping("/users/{userId}/bookings")
    public List<BookingResponseDTO> getBookingByUserId(@PathVariable Long userId) throws ResourceNotFoundException {
        return service.getBookingsByUserId(userId);
    }
}