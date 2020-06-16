package com.pma.server.mappers;

import com.pma.server.Dto.BuildingDto;
import com.pma.server.model.Building;

public class BuildingMapper {

    public static BuildingDto toBuildingDto(Building building){

        BuildingDto buildingDto = new BuildingDto();

        buildingDto.setAddress(building.getAddress());
        buildingDto.setId(building.getId());


        return buildingDto;
    }
}
