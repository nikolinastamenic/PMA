package com.pma.server.model.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Address implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String country;

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private String number;

    @Column
    private Long longitude;

    @Column
    private Long latitude;
}
