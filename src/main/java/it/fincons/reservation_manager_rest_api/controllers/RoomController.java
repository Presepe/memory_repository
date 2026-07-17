package it.fincons.reservation_manager_rest_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public List<RoomDto> getAll(){
        return service.getAll();
    }

    @Operation(summary = "Find room by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room found"),
            @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @GetMapping("/{id}")
    public RoomDto getById(@PathVariable Long id){
        return service.getById(id);
    }

    @Operation(summary = "Create new room")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Room created"),
            @ApiResponse(responseCode = "400", description = "Room not created")
    })
    @PostMapping
    public RoomDto create(@RequestBody RoomDto roomDto){
        return service.createRoom(roomDto);
    }

    @Operation(summary = "Update a room by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "400", description = "Room not updated")
    })

    @PutMapping("/{id}")
    public RoomDto update(@PathVariable Long id, @RequestBody RoomDto updatedRoom){
        return service.updateRoom(id, updatedRoom);
    }

    @Operation(summary = "Partially update a room by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "400", description = "Room not updated")
    })

    @PatchMapping("/{id}")
    public RoomDto patch(@PathVariable Long id, @RequestBody RoomDto updatedRoom){
        return service.patchRoom(id, updatedRoom);
    }

    @Operation(summary = "Delete a room by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room deleted"),
            @ApiResponse(responseCode = "400", description = "Room not deleted")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.deleteRoom(id);
    }
}
