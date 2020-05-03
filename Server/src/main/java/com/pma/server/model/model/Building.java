package com.pma.server.model.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Building implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    private Address address;


}
