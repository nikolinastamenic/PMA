package com.example.myapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class Address {


    @JsonProperty("id")
    private Long id;

    @JsonProperty("country")
    private String country;

    @JsonProperty("city")
    private String city;

    @JsonProperty("street")
    private String street;

    @JsonProperty("number")
    private String number;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("latitude")
    private double latitude;

}
