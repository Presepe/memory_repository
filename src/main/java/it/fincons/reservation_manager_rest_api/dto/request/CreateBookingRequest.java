package it.fincons.reservation_manager_rest_api.dto.request;

import it.fincons.reservation_manager_rest_api.validation.ValidTimeRange;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@ValidTimeRange
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {

    @NotNull(message = "La sala è obbligatoria")
    private Long roomId;

    @NotNull(message = "L'utente è obbligatorio")
    private Long userId;

    @NotNull(message = "La data deve essere obbligatoria")
    private LocalDate date;

    @NotNull(message = "L'orario d'inizio deve essere obbligatorio")
    private LocalTime startTime;

    @NotNull(message = "L'orario di fine è obbligatorio")
    private LocalTime endTime;
}
