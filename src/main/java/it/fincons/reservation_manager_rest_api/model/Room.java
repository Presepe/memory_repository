package it.fincons.reservation_manager_rest_api.model;

public class Room {
    private Long id;
    private String name;
    private Integer capacity;
    private Boolean hasProjector;

    public Room(Long id, String name, Integer capacity, Boolean hasProjector) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.hasProjector = hasProjector;
    }

    public Room() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getHasProjector() {
        return hasProjector;
    }

    public void setHasProjector(Boolean hasProjector) {
        this.hasProjector = hasProjector;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", hasProjector=" + hasProjector +
                '}';
    }
}
