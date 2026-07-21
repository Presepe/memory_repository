package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.request.CreateRoomRequest;
import it.fincons.reservation_manager_rest_api.dto.request.PatchRoomRequest;
import it.fincons.reservation_manager_rest_api.dto.response.RoomResponse;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.mappers.RoomMapper;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RoomMapper roomMapper;

    public RoomService(
            RoomRepository roomRepository,
            BookingRepository bookingRepository,
            RoomMapper roomMapper
    ) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.roomMapper = roomMapper;
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toDto)
                .toList();
    }

    public RoomResponse getRoomById(Long id) throws ResourceNotFoundException {
        Room room = getRoomEntityById(id);
        return roomMapper.toDto(room);
    }

    public RoomResponse createRoom(CreateRoomRequest request) {
        Room room = new Room();

        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setHasProjector(request.getHasProjector());

        Room savedRoom = roomRepository.save(room);
        return roomMapper.toDto(savedRoom);
    }

    public RoomResponse updateRoom(Long id, CreateRoomRequest request) throws ResourceNotFoundException {
        Room existingRoom = getRoomEntityById(id);

        existingRoom.setName(request.getName());
        existingRoom.setCapacity(request.getCapacity());
        existingRoom.setHasProjector(request.getHasProjector());

        Room updatedRoom = roomRepository.save(existingRoom);
        return roomMapper.toDto(updatedRoom);
    }

    public RoomResponse patchRoom(Long id, PatchRoomRequest request) throws ResourceNotFoundException {
        Room existingRoom = getRoomEntityById(id);

        if (request.getName() != null) {
            existingRoom.setName(request.getName());
        }

        if (request.getCapacity() != null) {
            existingRoom.setCapacity(request.getCapacity());
        }

        if (request.getHasProjector() != null) {
            existingRoom.setHasProjector(request.getHasProjector());
        }

        Room patchedRoom = roomRepository.save(existingRoom);
        return roomMapper.toDto(patchedRoom);
    }

    public void deleteRoom(Long id) throws ResourceInUseException, ResourceNotFoundException {
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

    // ========================================================================
    // METODI PRIVATI DI SUPPORTO E VALIDAZIONE
    // ========================================================================

    // Recupera direttamente l'Entity o lancia un'eccezione se non esiste
    private Room getRoomEntityById(Long id) throws ResourceNotFoundException {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala non trovata con id: " + id));
    }

    private void validateRoomExists(Long id) throws ResourceNotFoundException {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sala non trovata con id: " + id);
        }
    }
}