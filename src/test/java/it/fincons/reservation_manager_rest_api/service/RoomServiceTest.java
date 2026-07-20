package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.CreateRoomRequest;
import it.fincons.reservation_manager_rest_api.dto.PatchRoomRequest;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.model.User;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void getAllRooms_ShouldReturnAllRooms() {

        List<Room> rooms = List.of(new Room(1L, "Sala Leonardo", 8, true), new Room(2L, "Sala Galileo", 12, false));

        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.getAllRooms();

        assertEquals(2, result.size());
    }

    @Test
    void getRoomById_ShouldReturnRoom() throws ResourceNotFoundException {

        Room room = new Room(1L, "Sala Leonardo", 8, true);

        when(roomRepository.existsById(1L)).thenReturn(true);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room result = roomService.getRoomById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Sala Leonardo", result.getName());
    }

    @Test
    void getRoomById_ShouldThrowResourceNotFoundException() {

        when(roomRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomById(99L));
    }

    @Test
    void createRoom_ShouldSaveRoom() {

        CreateRoomRequest request = new CreateRoomRequest();
        request.setName("Sala Tesla");
        request.setCapacity(20);
        request.setHasProjector(true);

        Room savedRoom = new Room(1L, "Sala Tesla", 20, true);

        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);

        Room result = roomService.createRoom(request);

        assertEquals("Sala Tesla", result.getName());

        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void updateRoom_ShouldUpdateRoom() throws ResourceNotFoundException {

        Room room = new Room(1L, "Sala Leonardo", 8, true);

        CreateRoomRequest request = new CreateRoomRequest();

        request.setName("Sala Aggiornata");
        request.setCapacity(20);
        request.setHasProjector(false);

        when(roomRepository.existsById(1L)).thenReturn(true);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Room result = roomService.updateRoom(1L, request);

        assertEquals("Sala Aggiornata", result.getName());
        assertEquals(20, result.getCapacity());
        assertFalse(result.getHasProjector());
    }

    @Test
    void updateRoom_ShouldThrowNotFound() {

        when(roomRepository.existsById(99L)).thenReturn(false);

        CreateRoomRequest request = new CreateRoomRequest();

        assertThrows(ResourceNotFoundException.class, () -> roomService.updateRoom(99L, request));
    }

    @Test
    void patchRoom_ShouldUpdateOnlyProvidedFields() throws ResourceNotFoundException {

        Room room = new Room(1L, "Sala Leonardo", 8, true);

        PatchRoomRequest request = new PatchRoomRequest();

        request.setName("Sala Patchata");

        when(roomRepository.existsById(1L)).thenReturn(true);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Room result = roomService.patchRoom(1L, request);

        assertEquals("Sala Patchata", result.getName());
        assertEquals(8, result.getCapacity());
        assertTrue(result.getHasProjector());
    }

    @Test
    void patchRoom_ShouldThrowNotFound() {

        when(roomRepository.existsById(99L)).thenReturn(false);

        PatchRoomRequest request = new PatchRoomRequest();

        assertThrows(ResourceNotFoundException.class, () -> roomService.patchRoom(99L, request));
    }

    @Test
    void deleteRoom_ShouldDeleteRoom() throws ResourceInUseException, ResourceNotFoundException {

        when(roomRepository.existsById(1L)).thenReturn(true);

        when(bookingRepository.findByRoomId(1L)).thenReturn(Collections.emptyList());

        roomService.deleteRoom(1L);

        verify(roomRepository).deleteById(1L);
    }

    @Test
    void deleteRoom_ShouldThrowResourceInUseException() {

        Booking booking = new Booking(1L, Room.builder().id(1L).build(), User.builder().id(1L).build(), null, null, null);

        when(roomRepository.existsById(1L)).thenReturn(true);

        when(bookingRepository.findByRoomId(1L)).thenReturn(Collections.singletonList(booking));

        assertThrows(ResourceInUseException.class, () -> roomService.deleteRoom(1L));
    }

    @Test
    void deleteRoom_ShouldThrowNotFound() {

        when(roomRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> roomService.deleteRoom(99L));
    }
}
