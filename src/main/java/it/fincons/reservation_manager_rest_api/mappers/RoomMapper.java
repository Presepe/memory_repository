package it.fincons.reservation_manager_rest_api.mappers;

import it.fincons.reservation_manager_rest_api.dto.response.RoomResponse;
import it.fincons.reservation_manager_rest_api.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public RoomResponse toDto(Room room){
        if (room == null)
            return null;
        RoomResponse dto=new RoomResponse();
        dto.setId(room.getId());
        dto.setCapacity(room.getCapacity());
        dto.setName(room.getName());
        dto.setHasProjector(room.getHasProjector());
        return dto;
    }
}
