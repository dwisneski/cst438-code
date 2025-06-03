package com.cst438.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Date;

@Entity
public class Term {
    @Id
    @Column(name="term_id")
    private int termId;
    @Column(name="tyear")
    private int year;
    private String semester;

    //TODO add fields for columns add_date, add_deadline, drop_deadline, start_date and end_date
    // add get/set fields


}
