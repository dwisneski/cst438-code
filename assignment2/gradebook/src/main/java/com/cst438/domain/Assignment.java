package com.cst438.domain;

import jakarta.persistence.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="assignment_id")
    private int assignmentId;

    //TODO add fields for title and dueDate
    @ManyToOne
    @JoinColumn(name="section_no", nullable=false)
    private Section section;

    @OneToMany(mappedBy="assignment")
    private List<Grade> grades;

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    //TODO get/set methods for title and dueDate

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public List<Grade> getGrades() {
        return grades;
    }

}
