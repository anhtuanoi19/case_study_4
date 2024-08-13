package com.example.case_study_4.dto.request;

import com.example.case_study_4.entity.Course;
import com.example.case_study_4.entity.Student;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentCourseDto {

    private Long id;
    @Valid
    private Student student;
    @Valid
    private Course course;
    private Integer status;
}
