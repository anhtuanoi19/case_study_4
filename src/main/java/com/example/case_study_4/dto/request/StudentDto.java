package com.example.case_study_4.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDto {

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
