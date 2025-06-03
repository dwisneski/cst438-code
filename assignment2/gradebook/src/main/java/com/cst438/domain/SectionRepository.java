package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SectionRepository extends CrudRepository<Section, Integer> {
    @Query("select s from Section s " +
            "where s.instructorEmail=:email and s.term.year=:year and s.term.semester=:semester " +
            "order by s.course.courseId, s.secId")
    List<Section> findByInstructorEmailAndYearAndSemester(String email, int year, String semester);

}
