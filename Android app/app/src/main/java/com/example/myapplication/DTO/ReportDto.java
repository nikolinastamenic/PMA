package com.example.myapplication.DTO;


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
