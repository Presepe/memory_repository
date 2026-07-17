package it.fincons.reservation_manager_rest_api.service;

import it.fincons.reservation_manager_rest_api.dto.CreateUserRequest;
import it.fincons.reservation_manager_rest_api.exception.DuplicateEmailException;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
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

    public UserService(
            UserRepository userRepository,
            BookingRepository bookingRepository
    ) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        validateUserExists(id);

        return userRepository.findById(id).get();
    }

    public User createUser(CreateUserRequest request) {
        validateEmailUnique(request.getEmail());

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return userRepository.save(user);
    }

    public User updateUser(
            Long id,
            CreateUserRequest request
    ) {
        validateUserExists(id);

        User existingUser = userRepository.findById(id).get();

        if (!Objects.equals(
                existingUser.getEmail(),
                request.getEmail()
        )) {
            validateEmailUnique(request.getEmail());
        }

        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
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

    private void validateUserExists(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Utente non trovato con id: " + id
            );
        }
    }

    private void validateEmailUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(
                    "Esiste già un utente con e-mail: " + email
            );
        }
    }
}
