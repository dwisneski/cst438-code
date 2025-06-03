package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.EnrollmentDTO;
import com.cst438.service.RegistrarServiceProxy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;

@RestController
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;


    private final RegistrarServiceProxy registrar;

    public EnrollmentController (
            EnrollmentRepository enrollmentRepository,
            RegistrarServiceProxy registrar
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.registrar = registrar;
    }


    // instructor gets student enrollments with grades for a section
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    @GetMapping("/sections/{sectionNo}/enrollments")
    public List<EnrollmentDTO> getEnrollments(
            @PathVariable("sectionNo") int sectionNo, Principal principal ) {

        List<Enrollment> enrollments = enrollmentRepository
                .findEnrollmentsBySectionNoOrderByStudentName(sectionNo);

        //TODO check that logged in user is instructor for the section
        // return list of EnrollmentDTOs for the section
        return null;
    }

    // instructor updates enrollment grades
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    @PutMapping("/genrollments")
    public void updateEnrollmentGrade(@RequestBody List<EnrollmentDTO> dlist) {
        //TODO check that logged in user is instrctor for the section
        // update the enrollment grade
        // send message to Registrar service for grade update
    }
}
