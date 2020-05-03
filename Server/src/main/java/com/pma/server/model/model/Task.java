package com.pma.server.model.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Task implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Apartment apartment;

    @Column
    private TypeOfApartment typeOfApartment;

    @Column
    private State state;

    @Column
    private boolean urgent;

    @Column
    private Date deadline;

    @ManyToOne (cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private User user;

    @OneToOne
    private Report report;



}
