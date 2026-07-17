package it.fincons.reservation_manager_rest_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class CreateRoomRequest {
    @NotBlank(message = "Il nome della sale è obbligatorio")
    private String name;

    @NotNull(message = "Indicare se la proiezione esiste è obbligatorio ")
    private Boolean hasProjector;

    @NotNull(message = "La capacità è obbligatoria")
    @Min(value= 1, message = "La capacità deve essere almeno 1")
    private Integer capacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasProjector() {
        return hasProjector;
    }

    public void setHasProjector(Boolean hasProjector) {
        this.hasProjector = hasProjector;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
