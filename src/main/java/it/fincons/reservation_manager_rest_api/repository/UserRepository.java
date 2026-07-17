package it.fincons.reservation_manager_rest_api.repository;

import it.fincons.reservation_manager_rest_api.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final Map<Long, User> userMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return userMap.values()
                .stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public User save(User user) {

        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }

        userMap.put(user.getId(), user);

        return user;
    }

    public void deleteById(Long id) {
        userMap.remove(id);
    }

    public boolean existsById(Long id) {
        return userMap.containsKey(id);
    }
}
