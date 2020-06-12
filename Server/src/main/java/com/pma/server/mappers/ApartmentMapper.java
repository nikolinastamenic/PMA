package com.pma.server.mappers;

import com.pma.server.Dto.ApartmentDto;
import com.pma.server.model.Apartment;

public class ApartmentMapper {

    public static ApartmentDto toApartmentDto(Apartment apartment){

        ApartmentDto apartmentDto = new ApartmentDto();

        apartmentDto.setBuildingDto(BuildingMapper.toBuildingDto(apartment.getBuilding()));
        apartmentDto.setNumber(apartment.getNumber());
        apartmentDto.setId(apartment.getId());


        return apartmentDto;

    }
}
