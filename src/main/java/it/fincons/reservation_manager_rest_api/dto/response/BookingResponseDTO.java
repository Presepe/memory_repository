package it.fincons.reservation_manager_rest_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {

    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    // Invece di restituire gli oggetti completi Room e User,
    // restituiamo solo i dati che servono al client (es. ID e Nome).
    private Long roomId;
    private String roomName;
    private Long userId;
    private String userName;
}
