package com.pma.server.model.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Apartment implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private int number;

    @ManyToOne
    private Building building;

}
