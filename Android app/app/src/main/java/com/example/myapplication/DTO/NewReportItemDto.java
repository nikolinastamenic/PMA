package com.example.myapplication.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class NewReportItemDto {


    private int mySqlTaskId;

    private int mySqlReportId;

    private int mySqlReportItemId;

    private String reportCreatedDate;

    private String faultName;

    private String details;

    private PictureDto picture;
}
