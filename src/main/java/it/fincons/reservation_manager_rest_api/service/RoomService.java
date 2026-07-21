package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.CreateRoomRequest;
import it.fincons.reservation_manager_rest_api.dto.PatchRoomRequest;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.mapper.RoomMapper;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) throws ResourceNotFoundException {
        validateRoomExists(id);

        return roomRepository.findById(id).get();
    }

    public Room createRoom(CreateRoomRequest request) {
        Room room = RoomMapper.toEntity(request);

        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, CreateRoomRequest request) throws ResourceNotFoundException {

        getRoomById(id);

        Room room = RoomMapper.toEntity(request);
        room.setId(id);

        return roomRepository.save(room);
    }

    public Room patchRoom(Long id, PatchRoomRequest request) throws ResourceNotFoundException {
        validateRoomExists(id);

        Room existingRoom = roomRepository.findById(id).get();

        if (request.getName() != null) {
            existingRoom.setName(request.getName());
        }

        if (request.getCapacity() != null) {
            existingRoom.setCapacity(request.getCapacity());
        }

        if (request.getHasProjector() != null) {
            existingRoom.setHasProjector(request.getHasProjector());
        }

        return roomRepository.save(existingRoom);
    }

    public void deleteRoom(Long id) throws ResourceInUseException, ResourceNotFoundException {
        validateRoomExists(id);

        if (!bookingRepository.findByRoomId(id).isEmpty()) {
            throw new ResourceInUseException("Impossibile eliminare la sala con id " + id + " perché presenta delle prenotazioni");
        }

        roomRepository.deleteById(id);
    }

    private void validateRoomExists(Long id) throws ResourceNotFoundException {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sala non trovata con id: " + id);
        }
    }
}