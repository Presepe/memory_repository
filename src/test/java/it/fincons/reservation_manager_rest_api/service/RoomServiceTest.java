package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.request.CreateRoomRequest;
import it.fincons.reservation_manager_rest_api.dto.request.PatchRoomRequest;
import it.fincons.reservation_manager_rest_api.dto.response.RoomResponse;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.fixture.RoomFixture;
import it.fincons.reservation_manager_rest_api.fixture.UserFixture;
import it.fincons.reservation_manager_rest_api.mappers.RoomMapper;
import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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

    // Utilizziamo un'istanza reale del mapper invece di un mock
    @Spy
    private RoomMapper roomMapper = new RoomMapper();

    @InjectMocks
    private RoomService roomService;

    @Test
    void getAllRooms_ShouldReturnAllRooms() {

        List<Room> rooms = RoomFixture.createRoomList();

        when(roomRepository.findAll()).thenReturn(rooms);

        List<RoomResponse> result = roomService.getAllRooms();

        assertEquals(2, result.size());
    }

    @Test
    void getRoomById_ShouldReturnRoom() throws ResourceNotFoundException {

        Room room = RoomFixture.createValidEntity(1L);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        RoomResponse result = roomService.getRoomById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getRoomById_ShouldThrowResourceNotFoundException() {

        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> roomService.getRoomById(99L)
        );
    }

    @Test
    void createRoom_ShouldSaveRoom() {

        Room savedRoom = RoomFixture.createValidEntity(1L);

        CreateRoomRequest request = new CreateRoomRequest();
        request.setName(savedRoom.getName());
        request.setCapacity(savedRoom.getCapacity());
        request.setHasProjector(savedRoom.getHasProjector());

        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);

        RoomResponse result = roomService.createRoom(request);

        assertEquals(savedRoom.getName(), result.getName());

        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void updateRoom_ShouldUpdateRoom() throws ResourceNotFoundException {

        Room room = RoomFixture.createValidEntity(1L);

        CreateRoomRequest request = new CreateRoomRequest();
        request.setName("Sala Aggiornata");
        request.setCapacity(20);
        request.setHasProjector(false);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RoomResponse result = roomService.updateRoom(1L, request);

        assertEquals("Sala Aggiornata", result.getName());
        assertEquals(20, result.getCapacity());
        assertFalse(result.getHasProjector());
    }

    @Test
    void updateRoom_ShouldThrowNotFound() {

        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        CreateRoomRequest request = new CreateRoomRequest();

        assertThrows(
                ResourceNotFoundException.class,
                () -> roomService.updateRoom(99L, request)
        );
    }

    @Test
    void patchRoom_ShouldUpdateOnlyProvidedFields() throws ResourceNotFoundException {

        Room room = RoomFixture.createValidEntity(1L);

        PatchRoomRequest request = new PatchRoomRequest();
        request.setName("Sala Patchata");

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RoomResponse result = roomService.patchRoom(1L, request);

        assertEquals("Sala Patchata", result.getName());
        assertEquals(8, result.getCapacity());
        assertTrue(result.getHasProjector());
    }

    @Test
    void patchRoom_ShouldThrowNotFound() {

        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        PatchRoomRequest request = new PatchRoomRequest();

        assertThrows(
                ResourceNotFoundException.class,
                () -> roomService.patchRoom(99L, request)
        );
    }

    @Test
    void deleteRoom_ShouldDeleteRoom() throws ResourceInUseException, ResourceNotFoundException {

        // Qui manteniamo existsById perché il metodo deleteRoom() usa ancora validateRoomExists()
        when(roomRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.findByRoomId(1L)).thenReturn(Collections.emptyList());

        roomService.deleteRoom(1L);

        verify(roomRepository).deleteById(1L);
    }

    @Test
    void deleteRoom_ShouldThrowResourceInUseException() {

        Booking booking = new Booking(
                1L,
                RoomFixture.createValidEntity(1L),
                UserFixture.createValidEntity(1L),
                null,
                null,
                null
        );

        when(roomRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.findByRoomId(1L)).thenReturn(Collections.singletonList(booking));

        assertThrows(
                ResourceInUseException.class,
                () -> roomService.deleteRoom(1L)
        );
    }

    @Test
    void deleteRoom_ShouldThrowNotFound() {

        when(roomRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> roomService.deleteRoom(99L)
        );
    }
}