package com.example.myapplication.DTO;

import com.example.myapplication.model.Address;
import com.fasterxml.jackson.annotation.JsonProperty;


public class BuildingDto {

//    @JsonProperty("id")
    private Long id;

//    @JsonProperty("address")
    private Address address;

    public BuildingDto() {
    }

    public BuildingDto(Long id, Address address) {
        this.id = id;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
