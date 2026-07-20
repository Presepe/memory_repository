package it.fincons.reservation_manager_rest_api.fixture;

import it.fincons.reservation_manager_rest_api.dto.CreateUserRequest;

public class CreateUserRequestFixtures {

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
}