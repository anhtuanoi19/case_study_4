package com.example.case_study_4.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CreateStudentDto {
    private Long id;
    @NotBlank(message = "nameNotBlank")
    @Size(min = 3, max = 50, message = "checkSizeName")
    private String name;

    @NotBlank(message = "emailNotBlank")
    @Email(message = "emailExists")
    @Size(max = 255, message = "error.size")
    private String email;
    private Integer status;
    @Valid
    private List<@Valid CourseDto> courses;
}
