package it.fincons.reservation_manager_rest_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {

    @NotBlank(message = "Il nome dello user è obbligatorio")
    private String name;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Inserire un indirizzo email valido")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
