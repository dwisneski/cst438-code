package com.cst438.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/*
 * Data Transfer Object for course data
 */
public record CourseDTO(
        @NotNull(message="courseId required")
        @Pattern(regexp="^[a-zA-Z0-9-_]+$" , message="courseId must contain only letters, digits, hyphen or underscore.")
        String courseId,
        @NotNull(message="title required")
        @Pattern(regexp="^[a-zA-Z0-9-_ ]+$" , message="courseId must contain only letters, digits, hyphen, underscore or space.")
        String title,
        @Positive(message="credits must be positive")
        @Max(value=9, message="credits must be less than 10")
        int credits
) {
}
