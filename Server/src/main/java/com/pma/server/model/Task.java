package com.pma.server.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Task implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Apartment apartment;

    @Column
    private String typeOfApartment;

    @Column
    private String state;

    @Column
    private boolean urgent;

    @Column
    private Date deadline;

    @ManyToOne (cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private User user;

    @OneToOne
    private Report report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public String getTypeOfApartment() {
        return typeOfApartment;
    }

    public void setTypeOfApartment(String typeOfApartment) {
        this.typeOfApartment = typeOfApartment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
