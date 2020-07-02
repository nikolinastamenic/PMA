package com.pma.server.Dto;

import lombok.Data;

@Data
public class NewReportItemItemDto {

    private int mySqlTaskId;

    private int mySqlReportId;

    private int mySqlReportItemId;

    private String reportCreatedDate;

    private String faultName;

    private String details;

    private PictureDto picture;

    private int reportId;

    private int reportItemId;

}
