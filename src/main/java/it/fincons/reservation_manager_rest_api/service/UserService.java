package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.request.CreateUserRequest;
import it.fincons.reservation_manager_rest_api.dto.response.UserResponse;
import it.fincons.reservation_manager_rest_api.exception.DuplicateEmailException;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.mappers.UserMapper;
import it.fincons.reservation_manager_rest_api.model.User;
import it.fincons.reservation_manager_rest_api.repository.BookingRepository;
import it.fincons.reservation_manager_rest_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            BookingRepository bookingRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserResponse getUserById(Long id) throws ResourceNotFoundException {
        User user = getUserEntityById(id);
        return userMapper.toDto(user);
    }

    public UserResponse createUser(CreateUserRequest request) throws DuplicateEmailException {
        validateEmailUnique(request.getEmail());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserResponse updateUser(Long id, CreateUserRequest request) throws DuplicateEmailException, ResourceNotFoundException {
        User existingUser = getUserEntityById(id);

        if (!Objects.equals(existingUser.getEmail(), request.getEmail())) {
            validateEmailUnique(request.getEmail());
        }

        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    public void deleteUser(Long id) throws ResourceInUseException, ResourceNotFoundException {
        validateUserExists(id);

        if (!bookingRepository.findByUserId(id).isEmpty()) {
            throw new ResourceInUseException(
                    "Impossibile eliminare l'utente con id "
                            + id
                            + " perché presenta delle prenotazioni"
            );
        }

        userRepository.deleteById(id);
    }

    // ========================================================================
    // METODI PRIVATI DI SUPPORTO E VALIDAZIONE
    // ========================================================================

    // Recupera direttamente l'Entity o lancia un'eccezione se non esiste
    private User getUserEntityById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));
    }

    private void validateUserExists(Long id) throws ResourceNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utente non trovato con id: " + id);
        }
    }

    private void validateEmailUnique(String email) throws DuplicateEmailException {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Esiste già un utente con e-mail: " + email);
        }
    }
}