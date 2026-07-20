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
                    Room.builder().
                            name("Sala Bruno").
                            capacity(8).
                            hasProjector(true).
                            build()
            );

            Room room2 = roomRepository.save(
                    Room.builder().
                            name("Sala relax").
                            capacity(12).
                            hasProjector(false).
                            build()
            );

            User user1 = userRepository.save(
                    User.builder().
                            name("Mario Giordano").
                            email("mario.giordano@example.com").
                            build()
            );

            User user2 = userRepository.save(
                    User.builder().
                            name("Samuele Lamanna").
                            email("samuele.lamanna@example.com").
                            build()
            );

            bookingRepository.save(
                    new Booking(
                            null,
                            room1,
                            user1,
                            LocalDate.of(2026, 7, 20),
                            LocalTime.of(10, 0),
                            LocalTime.of(12, 0)
                    )
            );

            bookingRepository.save(
                    new Booking(
                            null,
                            room2,
                            user2,
                            LocalDate.of(2026, 7, 21),
                            LocalTime.of(14, 0),
                            LocalTime.of(16, 0)
                    )
            );
        };
    }
}