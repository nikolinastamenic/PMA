package com.example.myapplication.DTO;


import lombok.Data;

@Data
public class ReportItemDto {

    private Long id;

    private String faultName;

    private String details;

    private PictureDto picture;

    public ReportItemDto() {
    }


}
