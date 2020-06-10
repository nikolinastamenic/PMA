package com.pma.server.Dto;

import com.pma.server.model.Report;
import com.pma.server.model.ReportItem;
import com.pma.server.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportDto {

    private Long id;

    private List<ReportItemDto> itemList;

    private Date date;

    public ReportDto() {
    }

    public ReportDto(Long id, List<ReportItemDto> itemList, Date date) {
        this.id = id;
        this.itemList = itemList;
        this.date = date;
    }

    public ReportDto(Report report){
        this.id = report.getId();
        List<ReportItem> reportItems = report.getItemList();
        List<ReportItemDto> reportItemDtos = new ArrayList<>();
        for(ReportItem reportItem: reportItems) {
            ReportItemDto reportItemDto = new ReportItemDto(reportItem);
            reportItemDtos.add(reportItemDto);
        }
        this.itemList = reportItemDtos;
        this.date = report.getDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<ReportItemDto> getItemList() {
        return itemList;
    }

    public void setItemList(List<ReportItemDto> itemList) {
        this.itemList = itemList;
    }
}
