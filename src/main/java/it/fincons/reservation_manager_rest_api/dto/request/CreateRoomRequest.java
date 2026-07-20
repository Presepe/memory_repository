package it.fincons.reservation_manager_rest_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomRequest {
    @NotBlank(message = "Il nome della sale è obbligatorio")
    private String name;

    @NotNull(message = "Indicare se la proiezione esiste è obbligatorio ")
    private Boolean hasProjector;

    @NotNull(message = "La capacità è obbligatoria")
    @Min(value = 1, message = "La capacità deve essere almeno 1")
    private Integer capacity;
}
