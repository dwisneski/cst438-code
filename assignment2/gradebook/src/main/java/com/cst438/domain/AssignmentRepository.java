package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface AssignmentRepository extends CrudRepository<Assignment, Integer> {

    //TODO add query methods for
    // return list of assignments for a Section
    //   hint:  @Query("select a from Assignment a where a.section.sectionNo=:sectionNo order by a.dueDate")
    // return assignments for a student
    //   hint:   @Query("select a from Assignment a join a.section.enrollments e " +
    //          "where a.section.term.year=:year and a.section.term.semester=:semester and " +
    //          "e.student.id=:studentId order by a.dueDate")


}
