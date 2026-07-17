package it.fincons.reservation_manager_rest_api.dto;

public class PatchRoomRequest {

    private String name;

    private Boolean hasProjector;

    private Integer capacity;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boolean getHasProjector() {
        return hasProjector;
    }

    public void setHasProjector(Boolean hasProjector) {
        this.hasProjector = hasProjector;
    }


    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}