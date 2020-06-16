package com.pma.server.Dto;

import lombok.Data;

@Data
public class ReportItemDto {

    private Long id;
    private String faultName;
    private String details;
    private String picture;

}
