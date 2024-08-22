package com.example.case_study_4.controller;

import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.GetAllDto;
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
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    @Autowired
    private ICourseService service;

    @GetMapping("/getAll")
    public ApiResponse<Page<CourseDto>> getAllStudentsPageable(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        return service.getAllPageable(pageable, size, page);
    }

    @PostMapping("/create")
    public ApiResponse<CourseDto> create(@RequestBody @Valid CourseDto courseDto) {
        return service.create(courseDto);
    }

    @GetMapping("/findById/{id}")
    public ApiResponse<CourseDto> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<CourseDto> update(@RequestBody @Valid CourseDto courseDto, @PathVariable Long id) {
        return service.update(id, courseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @PostMapping("/xoa-mem/{id}")
    public ApiResponse<Boolean> deleteMem(@PathVariable Long id) {
        return service.deleteMem(id);
    }

    @PostMapping("/open/{id}")
    public ApiResponse<CourseDto> open(@PathVariable Long id) {
        return service.open(id);
    }

    @PostMapping("/search")
    public ApiResponse<Page<CourseDto>> search(
            @RequestParam(required = false) String title,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return service.findByTitle(title, page, size);
    }

}
