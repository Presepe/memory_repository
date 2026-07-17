package it.fincons.reservation_manager_rest_api.config;

import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.model.User;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.RoomRepository;
import it.fincons.reservation_manager_rest_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadData(
            RoomRepository roomRepository,
            UserRepository userRepository,
            BookingRepository bookingRepository) {

        return args -> {

            Room room1 = roomRepository.save(
                    new Room(
                            null,
                            "Sala Pescuma",
                            8,
                            true
                    )
            );

            Room room2 = roomRepository.save(
                    new Room(
                            null,
                            "Sala Relax",
                            12,
                            false
                    )
            );

            User user1 = userRepository.save(
                    new User(
                            null,
                            "Mario Giordano",
                            "mario.giordano@example.com"
                    )
            );

            User user2 = userRepository.save(
                    new User(
                            null,
                            "Samuele Lamanna",
                            "samuele.lamanna@example.com"
                    )
            );

            bookingRepository.save(
                    new Booking(
                            null,
                            room1.getId(),
                            user1.getId(),
                            LocalDate.of(2026, 7, 20),
                            LocalTime.of(10, 0),
                            LocalTime.of(12, 0)
                    )
            );

            bookingRepository.save(
                    new Booking(
                            null,
                            room2.getId(),
                            user2.getId(),
                            LocalDate.of(2026, 7, 21),
                            LocalTime.of(14, 0),
                            LocalTime.of(16, 0)
                    )
            );
        };
    }
}