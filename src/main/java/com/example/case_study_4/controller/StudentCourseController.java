package com.example.case_study_4.controller;

import com.example.case_study_4.dto.request.GetAllDto;
import com.example.case_study_4.dto.request.StudentCourseDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.service.IStudentCourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/student-corse")
public class StudentCourseController {

    @Autowired
    private IStudentCourseService studentCourseService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/all")
    public ApiResponse<Page<GetAllDto>> getAllStudentsCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            WebRequest request) {
        Locale locale = request.getHeader("Accept-Language") != null ?
                Locale.forLanguageTag(request.getHeader("Accept-Language")) : Locale.getDefault();
        return studentCourseService.getAll(page, size, locale);
    }

    @PostMapping("/search")
    public ApiResponse<Page<GetAllDto>> search(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            WebRequest request) {
        Locale locale = request.getHeader("Accept-Language") != null ?
                Locale.forLanguageTag(request.getHeader("Accept-Language")) : Locale.getDefault();
        return studentCourseService.searchByStudentName(name, page, size, locale);
    }

    @PostMapping("/create-all")
    public ApiResponse<StudentCourseDto> createAll(@RequestBody @Valid StudentCourseDto studentCourseDto, WebRequest request) {
        Locale locale = request.getHeader("Accept-Language") != null ?
                Locale.forLanguageTag(request.getHeader("Accept-Language")) : Locale.getDefault();
        return studentCourseService.createAll(studentCourseDto, locale);
    }

    @PutMapping("/update")
    public ApiResponse<StudentDto> updateStudent(@RequestBody @Valid StudentDto studentDto, WebRequest request) {
        Locale locale = request.getHeader("Accept-Language") != null ?
                Locale.forLanguageTag(request.getHeader("Accept-Language")) : Locale.getDefault();
        return studentCourseService.updateStudent(studentDto, locale);
    }

    @DeleteMapping("/soft-delete")
    public ApiResponse<StudentCourseDto> softDeleteStudentCourse(
            @RequestParam("studentId") Long studentId,
            @RequestParam("courseId") Long courseId,
            WebRequest request) {
        Locale locale = request.getHeader("Accept-Language") != null ?
                Locale.forLanguageTag(request.getHeader("Accept-Language")) : Locale.getDefault();
        return studentCourseService.xoaMem(studentId, courseId, locale);
    }

}
