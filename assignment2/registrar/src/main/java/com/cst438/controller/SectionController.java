package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.SectionDTO;
import com.cst438.service.GradebookServiceProxy;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SectionController {

    private final CourseRepository courseRepository;

    private final SectionRepository sectionRepository;

    private final TermRepository termRepository;

    private final UserRepository userRepository;

    private final GradebookServiceProxy gradebook;

    public SectionController(
            CourseRepository courseRepository,
            SectionRepository sectionRepository,
            TermRepository termRepository,
            UserRepository userRepository,
            GradebookServiceProxy gradebook
    ) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.termRepository = termRepository;
        this.userRepository = userRepository;
        this.gradebook = gradebook;
    }


    // ADMIN function to create a new section
    @PostMapping("/sections")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public SectionDTO addSection(@Valid @RequestBody SectionDTO section) {

        //TODO check that instructorEmail exists in database and is an INSTRUCTOR
        // create a Section entity related to course
        // return SectionDTO include the database generated primary key value
        // send message to GradeService using gradebook proxy
        return null;
    }

    // ADMIN function to update a section
    @PutMapping("/sections")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public SectionDTO updateSection(@Valid @RequestBody SectionDTO section) {
        //TODO validate and update fields instructorEmail
        //  update only fields instructorEmail, building, room, times
        //  send message to GradeService using gradebook proxy

    }

    // ADMIN function to create a delete section
    // delete will fail if there are related assignments or enrollments
    @DeleteMapping("/sections/{sectionno}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public void deleteSection(@PathVariable int sectionno) {
        //TODO delete the section entity
        //  send message to GradeService using gradebook proxy
    }


    // returns Sections for a course and term
    // courseId may be partial
    @GetMapping("/courses/{courseId}/sections")
    public List<SectionDTO> getSections(
            @PathVariable("courseId") String courseId,
            @RequestParam("year") int year ,
            @RequestParam("semester") String semester )  {

        List<Section> sections = sectionRepository.findByLikeCourseIdAndYearAndSemester(courseId+"%", year, semester);

        //TODO return list of SectionDTO
        return null;
    }

    // sections that are open for enrollment
    // today's date not before addDate and not after addDeadline
    @GetMapping("/sections/open")
    public List<SectionDTO> getOpenSectionsForEnrollment() {

        List<Section> sections = sectionRepository.findByOpenOrderByCourseIdSectionId();

        //TODO return list of SectionDTO
        return null;
    }
}
