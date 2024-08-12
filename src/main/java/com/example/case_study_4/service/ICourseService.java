package com.example.case_study_4.service;

import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICourseService {
    ApiResponse<Page<CourseDto>> getAllPageable(Pageable pageable, Integer size, Integer currentPage);
    ApiResponse<CourseDto> create(CourseDto courseDto);
    ApiResponse<CourseDto> findById(Long id);
    ApiResponse<CourseDto> update(Long id, CourseDto courseDto);
    ApiResponse<Boolean> delete(Long id);
    ApiResponse<CourseDto> open(Long id);
    ApiResponse<CourseDto> deleteMem(Long id);

}
