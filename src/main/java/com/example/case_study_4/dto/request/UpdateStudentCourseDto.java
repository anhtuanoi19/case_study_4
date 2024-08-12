package com.example.case_study_4.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class UpdateStudentCourseDto {

    @Min(value = 1, message = "idmin")
    private Long id;
    @NotBlank(message = "nameNotBlank")
    @Size(min = 3, max = 50, message = "checkSize")
    private String name;

    @NotBlank(message = "emailNotBlank")
    @Email(message = "emailExists")
    private String email;
    private Integer status;
    @Valid
    private List<Long> courseIds;
}
