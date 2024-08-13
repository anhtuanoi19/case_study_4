package com.example.case_study_4.service;

import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface IStudentService {

    ApiResponse<Page<StudentDto>> getAllPageable(Pageable pageable, Integer size, Integer currentPage, Locale locale);
    ApiResponse<StudentDto> create(StudentDto studentDto, Locale locale);
    ApiResponse<StudentDto> findById(Long id, Locale locale);
    ApiResponse<StudentDto> update(Long id, StudentDto studentDto, Locale locale);
    ApiResponse<Boolean> delete(Long id, Locale locale);
    ApiResponse<StudentDto> open(Long id, Locale locale);
    ApiResponse<StudentDto> deleteMem(Long id, Locale locale);
    ApiResponse<Page<StudentDto>> getAllStudentsWithCourses(int page, int size, Locale locale);
    ApiResponse<Void> deleteStudent(Long studentId, Locale locale);

}
