package com.example.case_study_4.service;

import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface IStudentService {

    ApiResponse<Page<StudentDto>> getAllPageable(Pageable pageable, Integer size, Integer currentPage);
    ApiResponse<StudentDto> create(StudentDto studentDto);
    ApiResponse<StudentDto> findById(Long id);
    ApiResponse<StudentDto> update(Long id, StudentDto studentDto);
    ApiResponse<Boolean> delete(Long id);
    ApiResponse<StudentDto> open(Long id);
    ApiResponse<StudentDto> deleteMem(Long id);
    ApiResponse<Page<StudentDto>> getAllStudentsWithCourses(int page, int size, Locale locale);
    ApiResponse<Void> deleteStudent(Long studentId);

}
