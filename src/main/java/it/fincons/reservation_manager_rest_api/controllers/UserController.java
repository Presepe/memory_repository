package it.fincons.reservation_manager_rest_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.fincons.reservation_manager_rest_api.exception.DuplicateEmailException;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.model.User;
import it.fincons.reservation_manager_rest_api.service.UserService;
import it.fincons.reservation_manager_rest_api.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Find all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }

    @Operation(summary = "Find user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public User  getUserById(@PathVariable Long id) throws ResourceNotFoundException {
        return service.getUserById(id);
    }

    @Operation(summary = "Create new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "User not created")
    })
    @PostMapping
    public User  createUser(@RequestBody CreateUserRequest  user ) throws DuplicateEmailException {
        return service.createUser(user);
    }

    @Operation(summary = "Update a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "User not updated")
    })
    @PutMapping("/{id}")
    public User  update(@PathVariable Long id, @RequestBody CreateUserRequest updatedUser) throws DuplicateEmailException, ResourceNotFoundException {
        return service.updateUser(id, updatedUser);
    }

    @Operation(summary = "Delete a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "User not deleted")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws ResourceInUseException, ResourceNotFoundException {
        service.deleteUser(id);
    }
}
