package com.cst438.domain;

import jakarta.persistence.*;

@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="enrollment_id")
    int enrollmentId;
    String grade;

    @ManyToOne
    @JoinColumn(name="section_no", nullable=false)
    Section section;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    User student;

    //TODO get/set methods for fields and relationships

}
