package it.fincons.reservation_manager_rest_api.fixture;

import it.fincons.reservation_manager_rest_api.dto.request.CreateBookingRequest;
import it.fincons.reservation_manager_rest_api.dto.response.BookingResponse;
import it.fincons.reservation_manager_rest_api.mappers.BookingMapper;
import it.fincons.reservation_manager_rest_api.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class BookingFixture {

    private static BookingMapper bookingMapper = new BookingMapper();

    public static Booking createValidEntity() {
        return new Booking(
                1L,
                RoomFixture.createValidEntity(100L),
                UserFixture.createValidEntity(100L),
                LocalDate.now().plusDays(1), // Prenotazione per domani
                LocalTime.of(10, 0),         // Dalle 10:00
                LocalTime.of(12, 0)          // Alle 12:00
        );
    }

    public static List<Booking> createEntityList() {
        return Arrays.asList(
                createValidEntity(),
                new Booking(
                        2L,
                        RoomFixture.createValidEntity(101L),
                        UserFixture.createValidEntity2(101L),
                        LocalDate.now().plusDays(2),
                        LocalTime.of(14, 0),
                        LocalTime.of(16, 0)
                )
        );
    }

    public static CreateBookingRequest createValidRequest() {
        CreateBookingRequest request = new CreateBookingRequest();
        request.setRoomId(100L);
        request.setUserId(200L);
        request.setDate(LocalDate.now().plusDays(1));
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));
        return request;
    }

    // 2. Errore di validazione standard (Manca la stanza)
    public static CreateBookingRequest createRequestWithoutRoom() {
        CreateBookingRequest request = createValidRequest();
        request.setRoomId(null); // Violerà @NotNull
        return request;
    }

    // 3. Errore di logica custom (Fine antecedente all'inizio)
    public static CreateBookingRequest createRequestWithInvalidTimeRange() {
        CreateBookingRequest request = createValidRequest();
        request.setStartTime(LocalTime.of(15, 0));
        request.setEndTime(LocalTime.of(10, 0)); // Violerà @ValidTimeRange
        return request;
    }

    public static BookingResponse createValidResponse(){
        return bookingMapper.toResponse(BookingFixture.createValidEntity());
    }

    public static List<BookingResponse> createResponseList(){
        return BookingFixture.createEntityList().stream().map(bookingMapper::toResponse).toList();
    }
}
