package it.fincons.reservation_manager_rest_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fincons.reservation_manager_rest_api.controllers.BookingController;
import it.fincons.reservation_manager_rest_api.dto.CreateBookingRequest;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.fixture.BookingFixture;
import it.fincons.reservation_manager_rest_api.fixture.CreateBookingRequestFixture;
import it.fincons.reservation_manager_rest_api.mapper.RoomMapper;
import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- TEST GET /api/bookings ---

    @Test
    void getAllBookings_shouldReturnListOfBookingsAnd200() throws Exception {
        List<Booking> mockBookings = BookingFixture.createBookingList();
        when(bookingService.getAllBookings()).thenReturn(mockBookings);

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockBookings.size()))
                .andExpect(jsonPath("$[0].id").value(mockBookings.get(0).getId()));
    }

    // --- TEST GET /api/bookings/{id} ---

    @Test
    void getBookingById_shouldReturnBookingAnd200_whenFound() throws Exception {

        Long bookingId = 1L;
        Booking mockBooking = BookingFixture.createValidBooking();

        when(bookingService.getBookingById(bookingId))
                .thenReturn(mockBooking);

        mockMvc.perform(get("/api/bookings/{id}", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(mockBooking.getId()))
                .andExpect(jsonPath("$.room.id")
                        .value(mockBooking.getRoom().getId()))
                .andExpect(jsonPath("$.user.id")
                        .value(mockBooking.getUser().getId()));
    }

    @Test
    void getBookingById_shouldReturn404_whenNotFound() throws Exception {
        Long bookingId = 99L;
        when(bookingService.getBookingById(bookingId))
                .thenThrow(new ResourceNotFoundException("Prenotazione non trovata"));

        mockMvc.perform(get("/api/bookings/{id}", bookingId))
                .andExpect(status().isNotFound());
    }

    // --- TEST POST /api/bookings ---

    @Test
    void createBooking_shouldReturnBookingAnd200_whenValidRequest() throws Exception {
        CreateBookingRequest request = CreateBookingRequestFixture.createValidRequest();
        Booking savedBooking = BookingFixture.createValidBooking();

        when(bookingService.createBooking(any(CreateBookingRequest.class))).thenReturn(savedBooking);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedBooking.getId()));
    }

    @Test
    void createBooking_shouldReturn400_whenValidationFails() throws Exception {
        CreateBookingRequest badRequest = CreateBookingRequestFixture.createRequestWithoutRoom();

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    // --- TEST PUT /api/bookings/{id} ---

    @Test
    void updateBooking_shouldReturnUpdatedBookingAnd200() throws Exception {
        Long bookingId = 1L;
        CreateBookingRequest request = CreateBookingRequestFixture.createValidRequest();
        Booking updatedBooking = BookingFixture.createValidBooking();

        when(bookingService.updateBooking(eq(bookingId), any(CreateBookingRequest.class))).thenReturn(updatedBooking);

        mockMvc.perform(put("/api/bookings/{id}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedBooking.getId()));
    }

    // --- TEST DELETE /api/bookings/{id} ---

    @Test
    void deleteBooking_shouldReturn200_whenSuccessful() throws Exception {
        Long bookingId = 1L;

        mockMvc.perform(delete("/api/bookings/{id}", bookingId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBooking_shouldReturn404_whenBookingNotFound() throws Exception {
        Long bookingId = 99L;
        doThrow(new ResourceNotFoundException("Prenotazione non trovata"))
                .when(bookingService).deleteBooking(bookingId);

        mockMvc.perform(delete("/api/bookings/{id}", bookingId))
                .andExpect(status().isNotFound());
    }

    // --- TEST GET /api/rooms/{roomId}/bookings ---

    @Test
    void getBookingByRoomId_shouldReturnBookingsAnd200() throws Exception {
        Long roomId = 100L;
        List<Booking> mockBookings = BookingFixture.createBookingList();
        when(bookingService.getBookingsByRoomId(roomId)).thenReturn(mockBookings);

        mockMvc.perform(get("/api/rooms/{roomId}/bookings", roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockBookings.size()));
    }

    // --- TEST GET /api/users/{user}/bookings ---

    @Test
    void getBookingByUserId_shouldReturnBookingsAnd200() throws Exception {
        Long userId = 200L;
        List<Booking> mockBookings = BookingFixture.createBookingList();
        when(bookingService.getBookingsByUserId(userId)).thenReturn(mockBookings);

        mockMvc.perform(get("/api/users/{user}/bookings", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockBookings.size()));
    }
}