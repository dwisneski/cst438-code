package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.EnrollmentDTO;
import com.cst438.service.GradebookServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class StudentScheduleController {

    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    private final SectionRepository sectionRepository;

    private final UserRepository userRepository;

    private final GradebookServiceProxy gradebook;

    public StudentScheduleController(
            EnrollmentRepository enrollmentRepository,
            SectionRepository sectionRepository,
            UserRepository userRepository,
            GradebookServiceProxy gradebook
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.gradebook = gradebook;
    }


    @PostMapping("/enrollments/sections/{sectionNo}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_STUDENT')")
    public EnrollmentDTO addCourse(
            @PathVariable int sectionNo,
            Principal principal ) {

        //TODO create and save an EnrollmentEntity
        //  relate enrollment to the student's User entity and to the Section entity
        //  check that student is not already enrolled in the section
        //  check that current data is not before addDate, not after addDeadline
        //  hint  Date now = new Date(); is the current date
        //  use now.before( ), now.after( ) methods
        // return EnrollmentDTO with primary key value from database

        return null;
    }

    // student drops a course
    @DeleteMapping("/enrollments/{enrollmentId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_STUDENT')")
    public void dropCourse(@PathVariable("enrollmentId") int enrollmentId, Principal principal) {

        User student = userRepository.findByEmail(principal.getName());
        if (student==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "student id not found");
        }
        //TODO the enrollment must belong to the logged in student
        // check that today is not after dropDeadline

    }

}
