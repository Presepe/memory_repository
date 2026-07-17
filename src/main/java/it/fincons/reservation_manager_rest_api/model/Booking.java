package it.fincons.reservation_manager_rest_api.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private Long id;
    private Long roomId;
    private Long userId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;


    public Booking(Long id, Long roomId, Long userId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Booking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", roomId='" + roomId + '\'' +
                ", userId=" + userId + '\'' +
                " , date=" + date + '\'' +
                " , startTime=" + startTime + '\'' +
                " , endTime=" + endTime +
                '}';
    }
}
