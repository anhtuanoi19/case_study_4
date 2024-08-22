package com.example.case_study_4.controller;

import com.example.case_study_4.dto.request.GetAllDto;
import com.example.case_study_4.dto.request.StudentCourseDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.request.UpdateStudentCourseDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.exception.AppException;
import com.example.case_study_4.exception.ErrorCode;
import com.example.case_study_4.service.IStudentCourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/student-corse")
public class StudentCourseController {

    @Autowired
    private IStudentCourseService studentCourseService;

    //Select native
//    @GetMapping("/all")
//    public ApiResponse<Page<GetAllDto>> getAllStudentsCourses(Pageable pageable) {
//        return studentCourseService.getAll(pageable);
//    }

    @GetMapping("/getAll")
    public ApiResponse<Page<GetAllDto>> getAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                               @RequestParam(value = "size", required = false, defaultValue = "5") Integer size)  {

        Pageable pageable = PageRequest.of(page, size);
        return studentCourseService.getAll(pageable);
    }

    //search native
    @PostMapping("/search")
    public ApiResponse<Page<GetAllDto>> search(
            @RequestParam(required = false) String name,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        return studentCourseService.searchByStudentName(name, pageable);
    }

    //create 1-n n-n 1-1


}
