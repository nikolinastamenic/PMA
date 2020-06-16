package com.pma.server.Dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ReportDto {

    private Long id;

    private List<ReportItemDto> itemList;

    private Date date;

    public ReportDto() {
            itemList = new ArrayList<>();
    }


}
