package com.pma.server.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ReportItem implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String faultName;

    @Column
    private String details;

    @Column
    private String picture;

    public ReportItem() {}

    public ReportItem(String faultName, String details, String picture) {
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
