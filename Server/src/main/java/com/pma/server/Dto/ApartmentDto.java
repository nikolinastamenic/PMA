package com.pma.server.Dto;

import lombok.Data;

@Data
public class ApartmentDto {

    private Long id;

    private int number;

    private BuildingDto buildingDto;

}
