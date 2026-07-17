package it.fincons.reservation_manager_rest_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @Operation(summary = "Find all bookings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings found"),
            @ApiResponse(responseCode = "404", description = "Bookings not found")
    })
    @GetMapping("/bookings")
    public List<BookingDto> getAll(){
        return service.getAll();
    }

    @Operation(summary = "Find booking by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking found"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/bookings/{id}")
    public BookingDto getById(@PathVariable Long id){
        return service.getById(id);
    }

    @Operation(summary = "Create new booking")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Booking created"),
            @ApiResponse(responseCode = "400", description = "Booking not created")
    })
    @PostMapping("/bookings")
    public BookingDto create(@RequestBody BookingDto bookingDto){
        return service.createBooking(bookingDto);
    }

    @Operation(summary = "Update a booking by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking updated"),
            @ApiResponse(responseCode = "400", description = "Booking not updated")
    })

    @PutMapping("/bookings/{id}")
    public BookingDto update(@PathVariable Long id, @RequestBody BookingDto updatedBooking){
        return service.updateBooking(id, updatedBooking);
    }

    @Operation(summary = "Delete a booking by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Booking deleted"),
            @ApiResponse(responseCode = "400", description = "Booking not deleted")
    })
    @DeleteMapping("/bookings/{id}")
    public void delete(@PathVariable Long id){
        service.deleteBooking(id);
    }

    @Operation(summary = "Find all bookings by room ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings found"),
            @ApiResponse(responseCode = "404", description = "Bookings not found")
    })
    @GetMapping("/rooms/{roomId}/bookings")
    public List<BookingDto> getBookingByRoomId(@PathVariable Long roomId){
        return service.getBookingByRoomId(roomId);
    }

    @Operation(summary = "Find all bookings by user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bookings found"),
            @ApiResponse(responseCode = "404", description = "Bookings not found")
    })
    @GetMapping("/users/{userId}/bookings")
    public List<BookingDto> getBookingByUserId(@PathVariable Long userId){
        return service.getBookingByUserId(userId);
    }
}