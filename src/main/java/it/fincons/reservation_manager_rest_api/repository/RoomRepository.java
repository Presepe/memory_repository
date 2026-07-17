package it.fincons.reservation_manager_rest_api.repository;

import it.fincons.reservation_manager_rest_api.model.Room;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RoomRepository {

    private final Map<Long, Room> rooms = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Room save(Room room) {

        if (room.getId() == null) {
            room.setId(idGenerator.getAndIncrement());
        }

        rooms.put(room.getId(), room);

        return room;
    }

    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(rooms.get(id));
    }

    public void deleteById(Long id) {
        rooms.remove(id);
    }

    public boolean existsById(Long id) {
        return rooms.containsKey(id);
    }
}