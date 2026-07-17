package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.CreateRoomRequest;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(
            RoomRepository roomRepository,
            BookingRepository bookingRepository
    ) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        validateRoomExists(id);

        return roomRepository.findById(id).get();
    }

    public Room createRoom(CreateRoomRequest request) {
        Room room = new Room();

        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setHasProjector(request.getHasProjector());

        return roomRepository.save(room);
    }

    public Room updateRoom(
            Long id,
            CreateRoomRequest request
    ) {
        validateRoomExists(id);

        Room existingRoom = roomRepository.findById(id).get();

        existingRoom.setName(request.getName());
        existingRoom.setCapacity(request.getCapacity());
        existingRoom.setHasProjector(request.getHasProjector());

        return roomRepository.save(existingRoom);
    }

    public Room patchRoom(
            Long id,
            CreateRoomRequest request
    ) {
        validateRoomExists(id);

        Room existingRoom = roomRepository.findById(id).get();

        if (request.getName() != null) {
            existingRoom.setName(request.getName());
        }

        if (request.getCapacity() != null) {
            existingRoom.setCapacity(request.getCapacity());
        }

        if (request.getHasProjector() != null) {
            existingRoom.setHasProjector(
                    request.getHasProjector()
            );
        }

        return roomRepository.save(existingRoom);
    }

    public void deleteRoom(Long id) {
        validateRoomExists(id);

        if (!bookingRepository.findByRoomId(id).isEmpty()) {
            throw new ResourceInUseException(
                    "Impossibile eliminare la sala con id "
                            + id
                            + " perché presenta delle prenotazioni"
            );
        }

        roomRepository.deleteById(id);
    }

    private void validateRoomExists(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Sala non trovata con id: " + id
            );
        }
    }
}