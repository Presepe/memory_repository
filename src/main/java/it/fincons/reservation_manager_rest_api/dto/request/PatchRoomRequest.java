package it.fincons.reservation_manager_rest_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchRoomRequest {

    private String name;

    private Boolean hasProjector;

    private Integer capacity;
}