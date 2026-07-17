package it.fincons.reservation_manager_rest_api.fixture;

import it.fincons.reservation_manager_rest_api.dto.CreateBookingRequest;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateBookingRequestFixture {

    // 1. Dati perfetti (Happy Path)
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
}