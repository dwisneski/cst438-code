package com.cst438.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="section_no")
    private int sectionNo;  // unique id assigned by database.  Used to enroll into a section.
    @ManyToOne
    @JoinColumn(name="course_id", nullable=false)
    private Course course;
    @ManyToOne
    @JoinColumn(name="term_id", nullable=false)
    private Term term;
    @Column(name="sec_id")
    private int secId;   // sequential numbering of sections of a course in a term:  1, 2, 3, ....
    //TODO complete fields for building, room, times and instructorEmail

    @OneToMany(mappedBy="section")
    List<Enrollment> enrollments;

    //TODO Generate get/set methods for all fields

}
