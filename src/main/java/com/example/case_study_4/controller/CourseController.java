package com.example.case_study_4.controller;

import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.repository.CourseRepository;
import com.example.case_study_4.service.ICourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    private ICourseService service;

    @Autowired
    private HttpServletRequest request;

    private Locale getLocale() {
        return request.getHeader("Accept-Language") != null ?
                Locale.forLanguageTag(request.getHeader("Accept-Language")) : Locale.getDefault();
    }

    @GetMapping()
    public ApiResponse<Page<CourseDto>> getAllStudentsPageable(
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "page", defaultValue = "0") Integer page) {

        Pageable pageable = PageRequest.of(page, size);
        Locale locale = getLocale();
        return service.getAllPageable(pageable, size, page, locale);
    }

    @PostMapping()
    public ApiResponse<CourseDto> create(@RequestBody @Valid CourseDto courseDto) {
        Locale locale = getLocale();
        return service.create(courseDto, locale);
    }

    @GetMapping("/findById/{id}")
    public ApiResponse<CourseDto> findById(@PathVariable Long id) {
        Locale locale = getLocale();
        return service.findById(id, locale);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<CourseDto> update(@RequestBody @Valid CourseDto courseDto, @PathVariable Long id) {
        Locale locale = getLocale();
        return service.update(id, courseDto, locale);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        Locale locale = getLocale();
        return service.delete(id, locale);
    }

    @PostMapping("/xoa-mem/{id}")
    public ApiResponse<CourseDto> deleteMem(@PathVariable Long id) {
        Locale locale = getLocale();
        return service.deleteMem(id, locale);
    }

    @PostMapping("/open/{id}")
    public ApiResponse<CourseDto> open(@PathVariable Long id) {
        Locale locale = getLocale();
        return service.open(id, locale);
    }
}
