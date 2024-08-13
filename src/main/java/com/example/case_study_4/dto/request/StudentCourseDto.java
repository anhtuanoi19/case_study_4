package com.example.case_study_4.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @NotEmpty(message = "Danh sách sinh viên không được để trống")
    @Valid
    private List<@Valid StudentDto> studentDtoList;

    @Valid
    private List<@Valid CourseDto> courseDtoList;
    private Integer status;
}
