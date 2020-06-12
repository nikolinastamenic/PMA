package com.example.myapplication.DTO;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportDto {

    private Long id;

    private List<ReportItemDto> itemList;

    private Date date;

    public ReportDto() {
        itemList = new ArrayList<>();
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
