package com.cst438.service;

import com.cst438.domain.*;
import com.cst438.dto.CourseDTO;
import com.cst438.dto.EnrollmentDTO;
import com.cst438.dto.SectionDTO;
import com.cst438.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class RegistrarServiceProxy {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    TermRepository termRepository;


    Queue registrarServiceQueue = new Queue("registrar_service", true);

    @Bean
    public Queue createQueue() {
        return new Queue("gradebook_service", true);
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "gradebook_service")
    public void receiveFromRegistrar(String message)  {
        System.out.println("received "+message);
        try {
            String part[] = message.split(" ", 2);
            switch (part[0]) {
                case "addCourse":
                    addCourse(fromJsonString(part[1], CourseDTO.class));
                    break;
                case "updateCourse":
                    updateCourse(fromJsonString(part[1], CourseDTO.class));
                    break;
                case "deleteCourse":
                    courseRepository.deleteById(part[1]);
                    break;
                case "addSection":
                    addSection(fromJsonString(part[1], SectionDTO.class));
                    break;
                case "updateSection":
                    updateSection(fromJsonString(part[1], SectionDTO.class));
                    break;
                case "deleteSection":
                    sectionRepository.deleteById(Integer.parseInt(part[1]));
                    break;
                case "addUser":
                    addUser(fromJsonString(part[1], UserDTO.class));
                    break;
                case "updateUser":
                    updateUser(fromJsonString(part[1], UserDTO.class));
                    break;
                case "deleteUser":
                    userRepository.deleteById(Integer.parseInt(part[1]));
                    break;
                case "addEnrollment":
                    addEnrollment(fromJsonString(part[1], EnrollmentDTO.class));
                    break;
                case "deleteEnrollment":
                    enrollmentRepository.deleteById(Integer.parseInt(part[1]));
                    break;
                default:
                    System.out.println("message type is unknown.");
            }
        } catch (Exception e) {
            System.out.println("Exception occurred in receiveFromRegistrar. "+e.getMessage());
        }
    }

    private void addCourse(CourseDTO dto) {
        Course c = new Course();
        c.setCourseId(dto.courseId());
        c.setCredits(dto.credits());
        c.setTitle(dto.title());
        courseRepository.save(c);
    }

    private void updateCourse(CourseDTO dto) {
        Course c = courseRepository.findById(dto.courseId()).orElse(null);
        if (c!=null) {
            c.setCredits(dto.credits());
            c.setTitle(dto.title());
            courseRepository.save(c);
        } else {
            addCourse(dto);
        }
    }

    private void addSection(SectionDTO dto) {
        Section s = new Section();
        s.setSectionNo(dto.secNo());
        s.setRoom(dto.room());
        s.setBuilding(dto.building());
        s.setInstructor_email(dto.instructorEmail());
        s.setSecId(dto.secId());
        s.setTimes(dto.times());
        Term t = termRepository.findByYearAndSemester(dto.year(), dto.semester());
        if (t==null) {
            System.out.println("Error.  Section without a term. "+dto);
            return;
        }
        s.setTerm(t);

        Course c = courseRepository.findById(dto.courseId()).orElse(null);
        if (c==null) {
            System.out.println("Error.  Section without a course. "+dto);
            return;
        }
        s.setCourse(c);
        sectionRepository.save(s);
    }

    private void updateSection(SectionDTO dto) {
        Section s = sectionRepository.findById(dto.secNo()).orElse(null);
        if (s == null) {
            addSection(dto);
            return;
        } else {
            s.setRoom(dto.room());
            s.setBuilding(dto.building());
            s.setInstructor_email(dto.instructorEmail());
            s.setSecId(dto.secId());
            s.setTimes(dto.times());
            // term and course cannot be changed in a section
            sectionRepository.save(s);
        }
    }

    private void addUser(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setName(dto.name());
        user.setType(dto.type());
        user.setPassword("");
        user.setId(dto.id());
        userRepository.save(user);
    }

    private void updateUser(UserDTO dto) {
        User user = userRepository.findById(dto.id()).orElse(null);
        if (user==null) {
            addUser(dto);
            return;
        } else {
            user.setEmail(dto.email());
            user.setName(dto.name());
            user.setType(dto.type());
            userRepository.save(user);
        }
    }

    private void addEnrollment(EnrollmentDTO dto) {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(dto.enrollmentId());
        Section s = sectionRepository.findById(dto.sectionNo()).orElse(null);
        if (s==null) {
            System.out.println("Error.  Enrollment without a section. "+dto);
            return;
        }
        enrollment.setSection(s);
        User u = userRepository.findById(dto.studentId()).orElse(null);
        if (u==null) {
            System.out.println("Error.  Enrollment without a user. " + dto);
            return;
        }
        enrollment.setStudent(u);
        enrollmentRepository.save(enrollment);
    }

    public void updateEnrollment(EnrollmentDTO dto) {
        String msg = "updateEnrollment " + asJsonString(dto);
        System.out.println(msg);
        sendMessage(msg);
    }

    private void sendMessage(String s) {
        rabbitTemplate.convertAndSend(registrarServiceQueue.getName(), s);
    }
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static <T> T  fromJsonString(String str, Class<T> valueType ) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}