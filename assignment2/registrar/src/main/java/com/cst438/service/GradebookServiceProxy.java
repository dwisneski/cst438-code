package com.cst438.service;

import com.cst438.domain.*;
import com.cst438.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class GradebookServiceProxy {

    private final EnrollmentRepository enrollmentRepository;
    private final RabbitTemplate rabbitTemplate;

    public GradebookServiceProxy(EnrollmentRepository enrollmentRepository, RabbitTemplate rabbitTemplate) {
        this.enrollmentRepository = enrollmentRepository;
        this.rabbitTemplate = rabbitTemplate;
    }
    Queue gradebookServiceQueue = new Queue("gradebook_service", true);

    @Bean
    public Queue createQueue() {
        return new Queue("registrar_service", true);
    }

    @RabbitListener(queues = "registrar_service")
    public void receiveFromGradebook(String message)  {
        try {
            System.out.println("registrar received " + message);
            //TODO process updateEnrollment messages from GradeBook

        } catch (Exception e) {
            System.out.println("exception "+e.getMessage());
        }
    }

    public void addCourse(CourseDTO dto) {
        String msg = "addCourse "+asJsonString(dto);
        System.out.println(msg);
        sendMessage(msg);
    }

    public void updateCourse(CourseDTO dto) {
        String msg = "updateCourse "+asJsonString(dto);
        System.out.println(msg);
        sendMessage(msg);
    }

    public void deleteCourse(String courseid) {
        String msg = "deleteCourse "+courseid;
        System.out.println(msg);
        sendMessage(msg);
    }


    private void sendMessage(String s) {
        rabbitTemplate.convertAndSend(gradebookServiceQueue.getName(), s);
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