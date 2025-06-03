package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.GradeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GradeController {
    private final AssignmentRepository assignmentRepository;

    private final EnrollmentRepository enrollmentRepository;

    private final GradeRepository gradeRepository;

    public GradeController (
            AssignmentRepository assignmentRepository,
            EnrollmentRepository enrollmentRepository,
            GradeRepository gradeRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.gradeRepository = gradeRepository;
    }
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    @GetMapping("/assignments/{assignmentId}/grades")
    public List<GradeDTO> getAssignmentGrades(@PathVariable("assignmentId") int assignmentId, Principal principal) {
        //TODO return a list of GradeDTOs containing student scores for an assignment
        // the user must the instructor for the assignment's section
        // if a Grade entity does not exist, then create the Grade entity with a null score.
        return null;
    }

    // user updates assignment grades
    @PutMapping("/grades")
    public void updateGrades(@RequestBody List<GradeDTO> dlist) {
        //TODO update the assignment score
        // the user must be the instructor for the assignment's section
        // if the Grade entity does not exist for the gradeId in the GradeDTO, return an exception and rollback
        //  any updates to the database
    }
}
