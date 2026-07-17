package it.fincons.reservation_manager_rest_api.fixture;

import it.fincons.reservation_manager_rest_api.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class BookingFixture {

    public static Booking createValidBooking() {
        return new Booking(
                1L,
                100L,
                200L,
                LocalDate.now().plusDays(1), // Prenotazione per domani
                LocalTime.of(10, 0),         // Dalle 10:00
                LocalTime.of(12, 0)          // Alle 12:00
        );
    }

    public static List<Booking> createBookingList() {
        return Arrays.asList(
                createValidBooking(),
                new Booking(2L, 101L, 201L, LocalDate.now().plusDays(2), LocalTime.of(14, 0), LocalTime.of(16, 0))
        );
    }
}
