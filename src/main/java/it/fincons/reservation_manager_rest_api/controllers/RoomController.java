package it.fincons.reservation_manager_rest_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.service.RoomService;
import it.fincons.reservation_manager_rest_api.dto.CreateRoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService service;

    @Operation(summary = "Find all rooms")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rooms found"),
            @ApiResponse(responseCode = "404", description = "Rooms not found")
    })
    @GetMapping
    public List<Room> getAllRooms(){
        return service.getAllRooms();
    }

    @Operation(summary = "Find room by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room found"),
            @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @GetMapping("/{id}")
    public Room  getRoomById(@PathVariable Long id) throws ResourceNotFoundException {
        return service.getRoomById(id);
    }

    @Operation(summary = "Create new room")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Room created"),
            @ApiResponse(responseCode = "400", description = "Room not created")
    })
    @PostMapping
    public Room  createRoom(@RequestBody CreateRoomRequest  room){
        return service.createRoom(room);
    }

    @Operation(summary = "Update a room by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "400", description = "Room not updated")
    })

    @PutMapping("/{id}")
    public Room  updateRoom(@PathVariable Long id, @RequestBody CreateRoomRequest  updatedRoom) throws ResourceNotFoundException {
        return service.updateRoom(id, updatedRoom);
    }

    @Operation(summary = "Partially update a room by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "400", description = "Room not updated")
    })

    @PatchMapping("/{id}")
    public Room  patch(@PathVariable Long id, @RequestBody CreateRoomRequest  updatedRoom) throws ResourceNotFoundException {
        return service.patchRoom(id, updatedRoom);
    }

    @Operation(summary = "Delete a room by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room deleted"),
            @ApiResponse(responseCode = "400", description = "Room not deleted")
    })
    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) throws ResourceInUseException, ResourceNotFoundException {
        service.deleteRoom(id);
    }
}
