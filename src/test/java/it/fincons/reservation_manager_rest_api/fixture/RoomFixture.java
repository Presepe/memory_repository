package it.fincons.reservation_manager_rest_api.fixture;

import it.fincons.reservation_manager_rest_api.dto.response.RoomResponse;
import it.fincons.reservation_manager_rest_api.mappers.RoomMapper;
import it.fincons.reservation_manager_rest_api.model.Room;

import java.util.List;

public class RoomFixture {
    private static RoomMapper roomMapper=new RoomMapper();

    public static Room createValidEntity(long id){
        return Room.builder().id(id).name("Sala Leonardo").capacity(8).hasProjector(true).build();
    }
    public static Room createValidEntity2(long id){
        return Room.builder().id(id).name("Sala Giochi").capacity(20).hasProjector(false).build();
    }

    public static List<Room> createRoomList(){
        return List.of(
                RoomFixture.createValidEntity(100L),
                RoomFixture.createValidEntity2(101L)
        );
    }

    public static RoomResponse createValidResponse(){
        return roomMapper.toDto(RoomFixture.createValidEntity(100L));
    }

    public static List<RoomResponse> createResponseList(){
        return RoomFixture.createRoomList().stream().map(roomMapper::toDto).toList();
    }
}
