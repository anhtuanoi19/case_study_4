package com.example.case_study_4.service;

import com.example.case_study_4.dto.request.CreateStudentDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.request.UpdateStudentCourseDto;
import com.example.case_study_4.dto.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface IStudentService {
    ApiResponse<StudentDto> findById(Long id);
    ApiResponse<StudentDto> open(Long id);
    ApiResponse<StudentDto> deleteMem(Long id);
    ApiResponse<Void> deleteStudent(Long studentId);
    ApiResponse<CreateStudentDto> createStudent(CreateStudentDto studentDto);
    ApiResponse<Void> deleteByStudentIdAndCourseId(Long studentId, Long courseId);
    ApiResponse<StudentDto> updateStudentAndCourses(UpdateStudentCourseDto dto);
    ApiResponse<Page<StudentDto>> search(String name, Pageable pageable);
    ApiResponse<Page<StudentDto>> getAllStudents(Pageable pageable);
}
