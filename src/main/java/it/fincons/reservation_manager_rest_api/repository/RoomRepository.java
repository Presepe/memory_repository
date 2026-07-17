import it.fincons.reservation_manager_rest_api.model.Room;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RoomRepository {
    private final Map<Long, Room> rooms = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public synchronized Room save(Room room) {

        Long id = room.id() == null
                ? sequence.incrementAndGet()
                : room.id();

        sequence.updateAndGet(current -> Math.max(current, id));

        Room saved = new Room(
                id,
                room.name(),
                room.capacity(),
                room.hasProjector()
        );

        rooms.put(id, saved);

        return saved;
    }

    public synchronized List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    public synchronized Optional<Room> findById(Long id) {
        return Optional.ofNullable(rooms.get(id));
    }

    public synchronized void deleteById(Long id) {
        rooms.remove(id);
    }

    public synchronized boolean existsById(Long id) {
        return rooms.containsKey(id);
    }
}