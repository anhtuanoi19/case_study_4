package com.example.case_study_4.service;

import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;

public interface ICourseService {
    ApiResponse<Page<CourseDto>> getAllPageable(Pageable pageable, Integer size, Integer currentPage, Locale locale);
    ApiResponse<CourseDto> create(CourseDto courseDto, Locale locale);
    ApiResponse<CourseDto> findById(Long id, Locale locale);
    ApiResponse<CourseDto> update(Long id, CourseDto courseDto, Locale locale);
    ApiResponse<Boolean> delete(Long id, Locale locale);
    ApiResponse<Boolean> deleteMem(Long courseId, Locale locale);
    ApiResponse<CourseDto> open(Long id, Locale locale);
    ApiResponse<Page<CourseDto>> findByTitle(String id, Locale locale, int page, int size);


}
