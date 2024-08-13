package com.example.case_study_4.controller;

import com.example.case_study_4.dto.request.GetAllDto;
import com.example.case_study_4.dto.request.StudentCourseDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.request.UpdateStudentCourseDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.service.IStudentCourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

@RestController
@RequestMapping("/api/student-corse")
public class StudentCourseController {

    @Autowired
    private IStudentCourseService studentCourseService;

    //Select native
    @GetMapping("/all")
    public ApiResponse<Page<GetAllDto>> getAllStudentsCourses(Pageable pageable) {
        return studentCourseService.getAll(pageable);
    }

    //search native
    @PostMapping("/search")
    public ApiResponse<Page<GetAllDto>> search(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return studentCourseService.searchByStudentName(name, pageable);
    }

    //create 1-n n-n 1-1


}
