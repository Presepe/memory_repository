package it.fincons.reservation_manager_rest_api.mapper;

import it.fincons.reservation_manager_rest_api.dto.CreateRoomRequest;
import it.fincons.reservation_manager_rest_api.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public static Room toEntity(CreateRoomRequest request) {

        return Room.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .hasProjector(request.getHasProjector())
                .build();
    }
}