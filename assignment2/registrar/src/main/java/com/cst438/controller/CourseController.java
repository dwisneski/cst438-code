package com.cst438.controller;

import com.cst438.domain.*;
import com.cst438.dto.CourseDTO;
import com.cst438.service.GradebookServiceProxy;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


/*
 * Controller for managing courses and sections.
 *   List courses, add course, update, delete course.
 *   List sections for a course, add, update and delete section.
 */
@RestController
public class CourseController {

    private final CourseRepository courseRepository;

    private final TermRepository termRepository;

    private final GradebookServiceProxy gradebook;

    public CourseController(
            CourseRepository courseRepository,
            TermRepository termRepository,
            GradebookServiceProxy gradebook
    ) {
        this.courseRepository = courseRepository;
        this.termRepository = termRepository;
        this.gradebook = gradebook;
    }


    // ADMIN function to create a new course
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/courses")
    public CourseDTO addCourse(@Valid @RequestBody CourseDTO course) {
        Course c = new Course();
        c.setCredits(course.credits());
        c.setTitle(course.title());
        c.setCourseId(course.courseId());
        courseRepository.save(c);
        CourseDTO courseDTO =  new CourseDTO(
                c.getCourseId(),
                c.getTitle(),
                c.getCredits()
        );
        gradebook.addCourse(courseDTO);
        return courseDTO;
    }

    // ADMIN function to update a course
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PutMapping("/courses")
    public CourseDTO updateCourse(@Valid @RequestBody CourseDTO course) {
        Course c = courseRepository.findById(course.courseId()).orElse(null);
        if (c==null) {
            throw  new ResponseStatusException( HttpStatus.NOT_FOUND, "course not found "+course.courseId());
        } else {
            c.setTitle(course.title());
            c.setCredits(course.credits());
            courseRepository.save(c);

            CourseDTO courseDTO =  new CourseDTO(
                    c.getCourseId(),
                    c.getTitle(),
                    c.getCredits()
            );
            gradebook.updateCourse(courseDTO);
            return courseDTO;
        }
    }

    // ADMIN function to delete a course
    // delete will fail if the course has sections
    @DeleteMapping("/courses/{courseid}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public void deleteCourse(@PathVariable String courseid) {
        Course c = courseRepository.findById(courseid).orElse(null);
        if (c != null ) {
            if (!c.getSections().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot delete course with sections");
            } else {
                gradebook.deleteCourse(courseid);
                courseRepository.delete(c);
            }
        }
    }

    @GetMapping("/courses")
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAllOrderByCourseIdAsc();
        List<CourseDTO> dto_list = new ArrayList<>();
        for (Course c : courses) {
            dto_list.add(new CourseDTO(c.getCourseId(), c.getTitle(), c.getCredits()));
        }
        return dto_list;
    }

}
