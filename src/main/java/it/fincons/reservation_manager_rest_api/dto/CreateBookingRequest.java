package it.fincons.reservation_manager_rest_api.dto;

import it.fincons.reservation_manager_rest_api.validation.ValidTimeRange;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@ValidTimeRange
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


    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
