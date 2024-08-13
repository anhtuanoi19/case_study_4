package com.example.case_study_4.dto.request;

import com.example.case_study_4.entity.StudentCoure;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto {

    private Long id;
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 3, max = 100, message = "Tiêu đề phải từ 3 đến 100 ký tự")
    private String title;
    @Size(max = 255, message = "error.size")
    private String description;
    private Integer status;
    private List<@Valid StudentDto> students;

}
