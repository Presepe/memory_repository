package it.fincons.reservation_manager_rest_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    //non serve fare @Column(nullable = false) perché questo caso è già
    //gestito da JoinColumn
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    //non serve fare @Column(nullable = false) perché questo caso è già
    //gestito da JoinColumn
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", roomId=" + room.getId() +
                ", userId=" + user.getId() +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
