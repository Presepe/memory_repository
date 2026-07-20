package it.fincons.reservation_manager_rest_api.fixture;

import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class BookingFixture {

    public static Booking createValidBooking() {
        return new Booking(
                1L,
                new Room(100L, "100LName", 100, false),
                new User(200L, "200LName", "200L@mail.com"),
                LocalDate.now().plusDays(1), // Prenotazione per domani
                LocalTime.of(10, 0),         // Dalle 10:00
                LocalTime.of(12, 0)          // Alle 12:00
        );
    }

    public static List<Booking> createBookingList() {
        return Arrays.asList(
                createValidBooking(),
                new Booking(2L, new Room(101L, "101LName", 101, false), new User(201L, "201LName", "201L@mail.com"), LocalDate.now().plusDays(2), LocalTime.of(14, 0), LocalTime.of(16, 0))
        );
    }
}
