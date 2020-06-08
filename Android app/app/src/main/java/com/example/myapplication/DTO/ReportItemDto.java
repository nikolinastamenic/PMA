package com.example.myapplication.DTO;


public class ReportItemDto {

    private Long id;

    private String faultName;

    private String details;

    private String picture;

    public ReportItemDto() {
    }


    public ReportItemDto(Long id, String faultName, String details, String picture) {
        this.id = id;
        this.faultName = faultName;
        this.details = details;
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFaultName() {
        return faultName;
    }

    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
