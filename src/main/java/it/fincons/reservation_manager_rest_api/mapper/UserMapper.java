package it.fincons.reservation_manager_rest_api.mapper;

import it.fincons.reservation_manager_rest_api.dto.CreateUserRequest;
import it.fincons.reservation_manager_rest_api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static User toEntity(CreateUserRequest request) {

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }

}