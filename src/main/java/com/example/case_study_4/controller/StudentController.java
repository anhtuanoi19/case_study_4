package com.example.case_study_4.controller;

import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.service.IStudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private IStudentService service;

    @Autowired
    private HttpServletRequest request;

    private Locale getLocale() {
        return request.getHeader("Accept-Language") != null ?
                Locale.forLanguageTag(request.getHeader("Accept-Language")) : Locale.getDefault();
    }

    @PostMapping()
    ApiResponse<StudentDto> create(@RequestBody @Valid StudentDto studentDto){
        Locale locale = getLocale();

        return service.create(studentDto, locale);
    }

    @GetMapping("/findById/{id}")
    ApiResponse<StudentDto> findById(@PathVariable Long id){
        Locale locale = getLocale();

        return service.findById(id, locale);
    }

    @PutMapping("/update/{id}")
    ApiResponse<StudentDto> update(@RequestBody @Valid StudentDto studentDto, @PathVariable Long id){
        Locale locale = getLocale();

        return service.update(id, studentDto,locale);
    }
    @DeleteMapping("delete/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        Locale locale = getLocale();

        return service.delete(id,locale);
    }

    @PostMapping("/xoa-mem/{id}")
    public ApiResponse<StudentDto> deleteMem(@PathVariable Long id){
        Locale locale = getLocale();

        return service.deleteMem(id,locale);
    }

    @PostMapping("/open/{id}")
    public ApiResponse<StudentDto> open(@PathVariable Long id){
        Locale locale = getLocale();

        return service.open(id, locale);
    }

    @GetMapping("/all")
    public ApiResponse<Page<StudentDto>> getAllStudentsWithCourses(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            WebRequest webRequest) {
        Locale locale = webRequest.getLocale();
        return service.getAllStudentsWithCourses(page, size, locale);
    }

    @DeleteMapping("/deleteStudent/{studentId}")
    public ApiResponse<Void> deleteStudent(@PathVariable Long studentId) {
        Locale locale = getLocale();
        return service.deleteStudent(studentId, locale);
    }



}
