package it.fincons.reservation_manager_rest_api.mappers;

import it.fincons.reservation_manager_rest_api.dto.response.UserResponse;
import it.fincons.reservation_manager_rest_api.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toDto(User user){
        if (user ==null)
            return null;
        UserResponse dto=new UserResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return dto;
    }
}
