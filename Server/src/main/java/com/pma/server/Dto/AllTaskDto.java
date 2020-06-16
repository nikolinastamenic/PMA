package com.pma.server.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class AllTaskDto {

    private Long id;
    private ApartmentDto apartmentDto;
    private String typeOfApartment;
    private String state;
    private boolean urgent;
    private Date deadline;
    private UserDto userDto;
    private ReportDto reportDto;

}
