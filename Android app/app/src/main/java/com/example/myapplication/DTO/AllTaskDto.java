package com.example.myapplication.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AllTaskDto {

//    @JsonProperty("id")
    private long id;

//    @JsonProperty("apartment_dto")
//    private ApartmentDto apartmentDto;

//    @JsonProperty("type_of_apartment")
    private String typeOfApartment;

//    @JsonProperty("state")
    private String state;

//    @JsonProperty("urgent")
    private boolean urgent;

//    @JsonProperty("deadline")
    private Date deadline;

    private ApartmentDto apartmentDto;

    public AllTaskDto() {
    }

//    public AllTaskDto(Long id, ApartmentDto apartmentDto, String typeOfApartment, String state, boolean urgent, Date deadline) {
//        this.id = id;
////        this.apartmentDto = apartmentDto;
//        this.typeOfApartment = typeOfApartment;
//        this.state = state;
//        this.urgent = urgent;
//        this.deadline = deadline;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
