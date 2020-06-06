package com.pma.server.Dto;

import java.util.Date;

public class AllTaskDto {

    private Long id;

    private ApartmentDto apartmentDto;

    private String typeOfApartment;

    private String state;

    private boolean urgent;

    private Date deadline;

    public AllTaskDto() {
    }

    public AllTaskDto(Long id, ApartmentDto apartmentDto, String typeOfApartment, String state, boolean urgent, Date deadline) {
        this.id = id;
        this.apartmentDto = apartmentDto;
        this.typeOfApartment = typeOfApartment;
        this.state = state;
        this.urgent = urgent;
        this.deadline = deadline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApartmentDto getApartmentDto() {
        return apartmentDto;
    }

    public void setApartmentDto(ApartmentDto apartmentDto) {
        this.apartmentDto = apartmentDto;
    }

    public String getTypeOfApartment() {
        return typeOfApartment;
    }

    public void setTypeOfApartment(String typeOfApartment) {
        this.typeOfApartment = typeOfApartment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
