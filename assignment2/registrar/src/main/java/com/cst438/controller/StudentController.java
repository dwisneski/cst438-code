package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.EnrollmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

   private final EnrollmentRepository enrollmentRepository;

   private final UserRepository userRepository;

   private final SectionRepository sectionRepository;

   public StudentController(
           EnrollmentRepository enrollmentRepository,
           UserRepository userRepository,
           SectionRepository sectionRepository
   ) {
       this.enrollmentRepository = enrollmentRepository;
       this.userRepository = userRepository;
       this.sectionRepository = sectionRepository;
   }

   // retrieve schedule for student for a term
   @GetMapping("/enrollments")
   @PreAuthorize("hasAuthority('SCOPE_ROLE_STUDENT')")
   public List<EnrollmentDTO> getSchedule(
           @RequestParam("year") int year,
           @RequestParam("semester") String semester,
           Principal principal) {
        User student = userRepository.findByEmail(principal.getName());
        if (student==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user id not found");
        }
        List<Enrollment> enrollments = enrollmentRepository.findByYearAndSemesterOrderByCourseId(year, semester, student.getId());

        //TODO return list of EnrollmentDTOs with all fields populated
       return null;
   }

   // return transcript for student
    @GetMapping("/transcripts")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_STUDENT')")
    public List<EnrollmentDTO> getTranscript(Principal principal) {

        User student = userRepository.findByEmail(principal.getName());
        if (student == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user id not found");
        }

        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByStudentIdOrderByTermId(student.getId());
        //TODO return list of EnrollmentDTOs with all fields populated
        return null;
    }
}