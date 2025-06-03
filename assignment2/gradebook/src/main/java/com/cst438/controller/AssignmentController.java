package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.AssignmentDTO;
import com.cst438.dto.AssignmentStudentDTO;
import com.cst438.dto.SectionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AssignmentController {

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    UserRepository userRepository;

    // get Sections for an instructor
    @GetMapping("/gsections")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    public List<SectionDTO> getSectionsForInstructor(
            @RequestParam("year") int year ,
            @RequestParam("semester") String semester,
            Principal principal)  {

        //TODO return the Sections that have instructorEmail for the user for the given term.
        return null;
    }

    // instructor lists assignments for a section.
    @GetMapping("/gsections/{secNo}/assignments")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    public List<AssignmentDTO> getAssignments(
            @PathVariable("secNo") int secNo,
            Principal principal) {

        //TODO verify that user is the instructor for the section
        //  return list of assignments
        return null;
    }


    @PostMapping("/assignments")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    public AssignmentDTO createAssignment(
            @RequestBody AssignmentDTO dto,
            Principal principal) {
        //TODO create and save an Assignment entity
        //  user must be the instructor for the Section
        //  return AssignmentDTO with database generated primary key
        return null;
    }


    @PutMapping("/assignments")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    public AssignmentDTO updateAssignment(@RequestBody AssignmentDTO dto, Principal principal) {
        //TODO update Assignment Entity.  only title and dueDate fields can be changed.
        //  user must be instructor of the Section
        return null;
    }


    @DeleteMapping("/assignments/{assignmentId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_INSTRUCTOR')")
    public void deleteAssignment(@PathVariable("assignmentId") int assignmentId, Principal principal) {
        //TODO verify that user is the instructor of the section
        // delete the Assignment entity
    }

    // student lists their assignments/grades  ordered by due date
    @GetMapping("/assignments")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_STUDENT')")
    public List<AssignmentStudentDTO> getStudentAssignments(
            @RequestParam("year") int year,
            @RequestParam("semester") String semester,
            Principal principal) {

        //TODO  return AssignmentStudentDTOs with scores (if the assignment has been graded)
        //  for the logged in student.  If assignment has not been graded, return a null score.
        return null;
    }
}
