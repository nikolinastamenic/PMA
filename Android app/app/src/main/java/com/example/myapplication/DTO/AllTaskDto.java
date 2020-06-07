package com.example.myapplication.DTO;


import java.util.Date;

public class AllTaskDto {

    private long id;

    private String typeOfApartment;

    private String state;

    private boolean urgent;

    private Date deadline;

    private ApartmentDto apartmentDto;

    private UserDto userDto;

    public AllTaskDto() {
    }


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

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
