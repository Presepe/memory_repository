package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.request.CreateUserRequest;
import it.fincons.reservation_manager_rest_api.dto.response.UserResponse;
import it.fincons.reservation_manager_rest_api.exception.DuplicateEmailException;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.fixture.UserFixture;
import it.fincons.reservation_manager_rest_api.mappers.UserMapper;
import it.fincons.reservation_manager_rest_api.model.Booking;
import it.fincons.reservation_manager_rest_api.model.User;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    // Inizializziamo l'oggetto reale per il mapper
    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    // --- TEST PER GET ALL USERS ---

    @Test
    @DisplayName("getAllUsers dovrebbe restituire la lista di tutti gli utenti")
    void getAllUsers_ShouldReturnUserList() {
        List<User> mockUsers = List.of(
                UserFixture.createValidEntity(1L),
                UserFixture.createValidEntity2(2L)
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    // --- TEST PER GET USER BY ID ---

    @Test
    @DisplayName("getUserById dovrebbe restituire l'utente se esiste")
    void getUserById_ShouldReturnUser_WhenUserExists() {
        User expectedUser = UserFixture.createValidEntity(1L);

        // Rimosso existsById
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Mario Rossi", result.getName());
    }

    @Test
    @DisplayName("getUserById dovrebbe lanciare ResourceNotFoundException se l'utente non esiste")
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {
        // Modificato per usare findById vuoto al posto di existsById
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }

    // --- TEST PER CREATE USER ---

    @Test
    @DisplayName("createUser dovrebbe salvare e restituire l'utente se l'email è univoca")
    void createUser_ShouldSaveUser_WhenEmailIsUnique() {
        CreateUserRequest request = UserFixture.createValidRequest();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        // Simula il salvataggio assegnando un ID all'oggetto
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        UserResponse result = userService.createUser(request);

        assertNotNull(result.getId());
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("createUser dovrebbe lanciare DuplicateEmailException se l'email è già in uso")
    void createUser_ShouldThrowException_WhenEmailExists() {
        CreateUserRequest request = UserFixture.createValidRequest();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any());
    }

    // --- TEST PER UPDATE USER ---

    @Test
    @DisplayName("updateUser dovrebbe aggiornare l'utente se i dati sono validi")
    void updateUser_ShouldUpdate_WhenDataIsValid() {
        User existingUser = UserFixture.createValidEntity(1L);
        CreateUserRequest updateRequest = UserFixture.createValidUpdateRequest();

        // Rimosso existsById
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(updateRequest.getEmail())).thenReturn(false);

        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponse result = userService.updateUser(1L, updateRequest);

        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("updateUser dovrebbe lanciare eccezione se l'utente non esiste")
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        CreateUserRequest updateRequest = UserFixture.createValidUpdateRequest();

        // Modificato per usare findById vuoto
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(99L, updateRequest));
    }

    @Test
    @DisplayName("updateUser dovrebbe lanciare eccezione se la nuova email è già usata da un altro utente")
    void updateUser_ShouldThrowException_WhenNewEmailIsDuplicated() {
        User existingUser = UserFixture.createValidEntity(1L);
        CreateUserRequest updateRequest = UserFixture.createValidUpdateRequest();

        // Rimosso existsById
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Simuliamo che la nuova email sia già presente a sistema
        when(userRepository.existsByEmail(updateRequest.getEmail())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.updateUser(1L, updateRequest));
        verify(userRepository, never()).save(any());
    }

    // --- TEST PER DELETE USER ---

    @Test
    @DisplayName("deleteUser dovrebbe eliminare l'utente se non ha prenotazioni attive")
    void deleteUser_ShouldDelete_WhenNoActiveBookings() {
        // Qui manteniamo existsById perché validateUserExists viene ancora chiamato dalla deleteUser
        when(userRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteUser dovrebbe lanciare ResourceInUseException se l'utente ha prenotazioni")
    void deleteUser_ShouldThrowException_WhenUserHasBookings() {
        when(userRepository.existsById(1L)).thenReturn(true);
        // Simuliamo la presenza di una prenotazione
        when(bookingRepository.findByUserId(1L)).thenReturn(List.of(new Booking()));

        assertThrows(ResourceInUseException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteUser dovrebbe lanciare eccezione se l'utente non esiste")
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(99L));
    }
}