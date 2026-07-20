package it.fincons.reservation_manager_rest_api.repository;

import it.fincons.reservation_manager_rest_api.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}