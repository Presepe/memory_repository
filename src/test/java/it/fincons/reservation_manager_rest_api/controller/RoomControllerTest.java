package it.fincons.reservation_manager_rest_api.controller;

import it.fincons.reservation_manager_rest_api.controllers.RoomController;
import it.fincons.reservation_manager_rest_api.exception.GlobalExceptionHandler;
import it.fincons.reservation_manager_rest_api.exception.ResourceInUseException;
import it.fincons.reservation_manager_rest_api.exception.ResourceNotFoundException;
import it.fincons.reservation_manager_rest_api.model.Room;
import it.fincons.reservation_manager_rest_api.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
@Import(GlobalExceptionHandler.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @Test
    void getAllRooms_ShouldReturnRooms() throws Exception {

        when(roomService.getAllRooms()).thenReturn(List.of(new Room(1L, "Sala Leonardo", 8, true)));

        mockMvc.perform(get("/api/rooms")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Sala Leonardo"));
    }

    @Test
    void getRoomById_ShouldReturnRoom() throws Exception {

        when(roomService.getRoomById(1L)).thenReturn(new Room(1L, "Sala Leonardo", 8, true));

        mockMvc.perform(get("/api/rooms/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createRoom_ShouldCreateRoom() throws Exception {

        when(roomService.createRoom(any())).thenReturn(new Room(1L, "Sala Leonardo", 8, true));

        String json = """
                {
                  "name":"Sala Leonardo",
                  "capacity":8,
                  "hasProjector":true
                }
                """;

        mockMvc.perform(post("/api/rooms").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createRoom_ShouldReturnBadRequest_WhenNameMissing() throws Exception {

        String json = """
                {
                  "capacity":8,
                  "hasProjector":true
                }
                """;

        mockMvc.perform(post("/api/rooms").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    void createRoom_ShouldReturnBadRequest_WhenCapacityInvalid() throws Exception {

        String json = """
                {
                  "name":"Sala Test",
                  "capacity":0,
                  "hasProjector":true
                }
                """;

        mockMvc.perform(post("/api/rooms").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    void updateRoom_ShouldReturnUpdatedRoom() throws Exception {

        when(roomService.updateRoom(eq(1L), any())).thenReturn(new Room(1L, "Sala Aggiornata", 20, false));

        String json = """
                {
                  "name":"Sala Aggiornata",
                  "capacity":20,
                  "hasProjector":false
                }
                """;

        mockMvc.perform(put("/api/rooms/1").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Sala Aggiornata"));
    }

    @Test
    void updateRoom_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {

        String json = """
                {
                  "name":"",
                  "capacity":0,
                  "hasProjector":true
                }
                """;

        mockMvc.perform(put("/api/rooms/1").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    void patchRoom_ShouldReturnUpdatedRoom() throws Exception {

        when(roomService.patchRoom(eq(1L), any())).thenReturn(new Room(1L, "Sala Patchata", 8, true));

        String json = """
                {
                  "name":"Sala Patchata"
                }
                """;

        mockMvc.perform(patch("/api/rooms/1").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Sala Patchata"));
    }

    @Test
    void deleteRoom_ShouldReturnOk() throws Exception {

        doNothing().when(roomService).deleteRoom(1L);

        mockMvc.perform(delete("/api/rooms/1")).andExpect(status().isOk());
    }

    @Test
    void getRoomById_ShouldReturnNotFound()
            throws Exception {

        when(roomService.getRoomById(99L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Sala non trovata con id: 99"
                        )
                );

        mockMvc.perform(get("/api/rooms/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRoom_ShouldReturnNotFound()
            throws Exception {

        doThrow(
                new ResourceNotFoundException(
                        "Sala non trovata con id: 99"
                )
        ).when(roomService).deleteRoom(99L);

        mockMvc.perform(delete("/api/rooms/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRoom_ShouldReturnConflict()
            throws Exception {

        doThrow(
                new ResourceInUseException(
                        "Sala occupata"
                )
        ).when(roomService).deleteRoom(1L);

        mockMvc.perform(delete("/api/rooms/1"))
                .andExpect(status().isConflict());
    }

}