package it.fincons.reservation_manager_rest_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.fincons.reservation_manager_rest_api.controllers.UserController;
import it.fincons.reservation_manager_rest_api.dto.request.CreateUserRequest;
import it.fincons.reservation_manager_rest_api.dto.response.UserResponse;
import it.fincons.reservation_manager_rest_api.exception.DuplicateEmailException;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.fixture.UserFixture;
import it.fincons.reservation_manager_rest_api.model.User;
import it.fincons.reservation_manager_rest_api.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    // --- TEST PER GET ALL USERS ---

    @Test
    @DisplayName("GET /api/users dovrebbe restituire 200 OK e la lista degli utenti")
    void getAllUsers_ShouldReturn200AndUserList() throws Exception {
        List<UserResponse> mockUsers = UserFixture.createResponseList();
        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Mario Rossi"))
                .andExpect(jsonPath("$[1].name").value("Luigi Verdi"));
    }

    @Test
    @DisplayName("GET /api/users dovrebbe restituire 200 OK e una lista vuota se non ci sono utenti")
    void getAllUsers_ShouldReturn200AndEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    // --- TEST PER GET USER BY ID ---

    @Test
    @DisplayName("GET /api/users/{id} dovrebbe restituire 200 OK e l'utente se esiste")
    void getUserById_ShouldReturn200AndUser_WhenUserExists() throws Exception {
        UserResponse expectedUser = UserFixture.createValidResponse();
        when(userService.getUserById(eq(expectedUser.getId()))).thenReturn(expectedUser);

        mockMvc.perform(get("/api/users/" + expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUser.getId()))
                .andExpect(jsonPath("$.name").value("Mario Rossi"));
    }

    @Test
    @DisplayName("GET /api/users/{id} dovrebbe restituire 404 Not Found se l'utente non esiste")
    void getUserById_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new ResourceNotFoundException("Utente non trovato"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- TEST PER CREATE USER ---

    @Test
    @DisplayName("POST /api/users dovrebbe restituire 200 OK e creare l'utente se valido")
    void createUser_ShouldReturn200AndCreatedUser() throws Exception {
        CreateUserRequest request = UserFixture.createValidRequest();
        UserResponse expectedUser = UserFixture.createValidResponse();

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(expectedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUser.getId()))
                .andExpect(jsonPath("$.email").value("mario.rossi@email.com"));
    }

    @Test
    @DisplayName("POST /api/users dovrebbe restituire 400 Bad Request se i dati non sono validi")
    void createUser_ShouldReturn400_WhenDataIsInvalid() throws Exception {
        // Creiamo una request vuota che fallirà sicuramente i controlli @NotBlank e @Email
        CreateUserRequest invalidRequest = new CreateUserRequest();
        invalidRequest.setName("");
        invalidRequest.setEmail("email-non-valida");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verifichiamo che il service non venga mai chiamato se i dati sono invalidi
        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("POST /api/users dovrebbe restituire 409 Conflict se l'email è duplicata")
    void createUser_ShouldReturn409_WhenEmailIsDuplicated() throws Exception {
        CreateUserRequest request = UserFixture.createValidRequest();

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenThrow(new DuplicateEmailException("Email già in uso"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    // --- TEST PER UPDATE USER ---

    @Test
    @DisplayName("PUT /api/users/{id} dovrebbe restituire 200 OK e aggiornare l'utente")
    void updateUser_ShouldReturn200AndUpdatedUser() throws Exception {
        CreateUserRequest request = UserFixture.createValidUpdateRequest();
        UserResponse expectedUser = new UserResponse(1L, request.getName(), request.getEmail());

        when(userService.updateUser(eq(1L), any(CreateUserRequest.class))).thenReturn(expectedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mario Rossi Aggiornato"));
    }

    @Test
    @DisplayName("PUT /api/users/{id} dovrebbe restituire 400 Bad Request se i dati non sono validi")
    void updateUser_ShouldReturn400_WhenDataIsInvalid() throws Exception {
        CreateUserRequest invalidRequest = new CreateUserRequest();
        invalidRequest.setName(null); // Fallirà @NotBlank

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(any(), any());
    }

    @Test
    @DisplayName("PUT /api/users/{id} dovrebbe restituire 404 Not Found se l'utente non esiste")
    void updateUser_ShouldReturn404_WhenUserDoesNotExist() throws Exception {
        CreateUserRequest request = UserFixture.createValidUpdateRequest();

        when(userService.updateUser(eq(99L), any(CreateUserRequest.class)))
                .thenThrow(new ResourceNotFoundException("Utente non trovato"));

        mockMvc.perform(put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // --- TEST PER DELETE USER ---

    @Test
    @DisplayName("DELETE /api/users/{id} dovrebbe restituire 200 OK")
    void deleteUser_ShouldReturn200() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("DELETE /api/users/{id} dovrebbe restituire 409 Conflict se l'utente ha prenotazioni attive")
    void deleteUser_ShouldReturn409_WhenUserHasBookings() throws Exception {
        doThrow(new ResourceInUseException("Impossibile eliminare l'utente"))
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}