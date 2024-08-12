package com.example.case_study_4.dto.request;

import com.example.case_study_4.entity.StudentCoure;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CourseDto {

    private Long id;
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 3, max = 100, message = "Tiêu đề phải từ 3 đến 100 ký tự")
    private String title;
    private String description;
    private Integer status;
    private List<@Valid StudentDto> students;


}
