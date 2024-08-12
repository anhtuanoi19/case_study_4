package com.example.case_study_4.dto.request;

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
public class StudentCourseDto {

    private Long id;

    @NotEmpty(message = "Danh sách sinh viên không được để trống")
    @Valid
    private List<StudentDto> studentDtoList;

    @NotEmpty(message = "Danh sách khóa học không được để trống")
    @Valid
    private List<CourseDto> courseDtoList;
    private Integer status;
}
