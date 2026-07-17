package it.fincons.reservation_manager_rest_api.repository;


import  it.fincons.reservation_manager_rest_api.model.Booking;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BookingRepository {

    private final Map<Long, Booking> bookings = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public BookingRepository() {

        // Dato iniziale
        Booking booking = new Booking();

        booking.setRoomId(1L);
        booking.setUserId(1L);
        booking.setDate(LocalDate.of(2026, 7, 20));
        booking.setStartTime(LocalTime.of(10, 0));
        booking.setEndTime(LocalTime.of(12, 0));

        save(booking);
    }

    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(bookings.get(id));
    }

    public Booking save(Booking booking) {

        if (booking.getId() == null) {
            booking.setId(idGenerator.getAndIncrement());
        }

        bookings.put(booking.getId(), booking);

        return booking;
    }

    public boolean existsById(Long id) {
        return bookings.containsKey(id);
    }

    public void deleteById(Long id) {
        bookings.remove(id);
    }

    public List<Booking> findByRoomId(Long roomId) {

        return bookings.values()
                .stream()
                .filter(booking -> booking.getRoomId().equals(roomId))
                .toList();
    }

    public List<Booking> findByUserId(Long userId) {

        return bookings.values()
                .stream()
                .filter(booking -> booking.getUserId().equals(userId))
                .toList();
    }

}