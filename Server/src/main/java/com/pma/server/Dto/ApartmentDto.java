package com.pma.server.Dto;


public class ApartmentDto {

    private Long id;

    private int number;

    private BuildingDto buildingDto;

    public ApartmentDto() {}

    public ApartmentDto(Long id, int number, BuildingDto buildingDto) {
        this.id = id;
        this.number = number;
        this.buildingDto = buildingDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public BuildingDto getBuildingDto() {
        return buildingDto;
    }

    public void setBuildingDto(BuildingDto buildingDto) {
        this.buildingDto = buildingDto;
    }
}
