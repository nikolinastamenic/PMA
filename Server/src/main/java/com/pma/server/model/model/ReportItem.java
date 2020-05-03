package com.pma.server.model.model;

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

}
