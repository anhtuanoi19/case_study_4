package com.example.case_study_4.service;

import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.GetAllDto;
import com.example.case_study_4.dto.request.StudentCourseDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.entity.StudentCoure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;

public interface IStudentCourseService {
    ApiResponse<Page<GetAllDto>> getAll(int page, int size, Locale locale);

    ApiResponse<Page<GetAllDto>> searchByStudentName(String name, int page, int size, Locale locale) ;

    ApiResponse<StudentCourseDto> createAll(StudentCourseDto studentCourseDto, Locale locale);
    ApiResponse<StudentDto> updateStudent(StudentDto studentDto, Locale locale);
    ApiResponse<StudentCourseDto> xoaMem(Long studentId, Long courseId, Locale locale);
}
