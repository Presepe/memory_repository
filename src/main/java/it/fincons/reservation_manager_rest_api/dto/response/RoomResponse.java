package it.fincons.reservation_manager_rest_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private long id;
    private String name;
    private Boolean hasProjector;
    private Integer capacity;
}
