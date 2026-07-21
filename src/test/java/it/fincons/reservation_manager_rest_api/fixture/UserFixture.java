package it.fincons.reservation_manager_rest_api.fixture;

import it.fincons.reservation_manager_rest_api.dto.request.CreateUserRequest;
import it.fincons.reservation_manager_rest_api.dto.response.UserResponse;
import it.fincons.reservation_manager_rest_api.mappers.RoomMapper;
import it.fincons.reservation_manager_rest_api.mappers.UserMapper;
import it.fincons.reservation_manager_rest_api.model.User;

import java.util.List;

public class UserFixture {

    public static UserMapper userMapper=new UserMapper();

    public static User createValidEntity(Long id) {
        return User.builder().id(id).name("Mario Rossi").email("mario.rossi@email.com").build();
    }

    public static User createValidEntity2(Long id) {
        return User.builder().id(id).name("Luigi Verdi").email("luigi.rossi@email.com").build();
    }

    public static List<User> createEntityList(){
        return List.of(
                UserFixture.createValidEntity(200L),
                UserFixture.createValidEntity2(201L)
        );
    }

    public static CreateUserRequest createValidRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Mario Rossi");
        request.setEmail("mario.rossi@email.com");
        return request;
    }

    public static CreateUserRequest createValidUpdateRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Mario Rossi Aggiornato");
        request.setEmail("mario.nuova@email.com");
        return request;
    }

    public static UserResponse createValidResponse(){
        return userMapper.toDto(UserFixture.createValidEntity(100L));
    }

    public static List<UserResponse> createResponseList(){
        return UserFixture.createEntityList().stream().map(userMapper::toDto).toList();
    }
}