package it.fincons.reservation_manager_rest_api.repository;

import it.fincons.reservation_manager_rest_api.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final Map<Long,User> userMap = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserRepository()
    {
        User user = new User();
        user.setName("Ciro");
        user.setEmail("ciroesposito@napolimail.napoli.it");

        save(user);
    }

    public List<User> findAll(){
        return new List<User>(userMap.values());
    }
    public User findById(Long id)
    {
        return userMap.get(id);
    }
    public User findByEmail(String email){
        List<User> usersList = userMap.values();
        for (User user : usersList) {
            if(user.getEmail().equals(email))
                return user;
        }
        return null;
    }
    public User save(User user){
        if(user.getId() == null)
            user.setId(idGenerator.getAndIncrement());
    }
    public void deleteById(Long id){
        userMap.remove(id);
    }
    public Boolean existsById(Long id){
        return userMap.containsKey(id);
    }
}
