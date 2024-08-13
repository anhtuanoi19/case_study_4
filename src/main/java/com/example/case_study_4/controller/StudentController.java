package com.example.case_study_4.controller;

import com.example.case_study_4.dto.request.*;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private IStudentService service;

    @GetMapping("/search")
    public ApiResponse<Page<StudentDto>> searchStudents(
            @RequestParam(value = "name", required = false) String name,
            Pageable pageable) {
        return service.search(name, pageable);
    }

    @GetMapping
    public ApiResponse<Page<StudentDto>> getAll(@RequestParam("page") Optional<Integer> page,
                                                @RequestParam("size") Optional<Integer> size) {
        Pageable pageable = PageRequest.of(
                page.orElse(0),
                size.orElse(10)
        );
        return service.getAllStudents(pageable);
    }

    @GetMapping("/findById/{id}")
    ApiResponse<StudentDto> findById(@PathVariable Long id){

        return service.findById(id);
    }

    @PostMapping("/xoa-mem/{id}")
    public ApiResponse<StudentDto> deleteMem(@PathVariable Long id){

        return service.deleteMem(id);
    }

    @PostMapping("/open/{id}")
    public ApiResponse<StudentDto> open(@PathVariable Long id){

        return service.open(id);
    }

    @DeleteMapping("/deleteStudent/{studentId}")
    public ApiResponse<Void> deleteStudent(@PathVariable Long studentId) {
        return service.deleteStudent(studentId);
    }
    @PostMapping("/create-all")
    public ApiResponse<CreateStudentDto> createAll(@RequestBody @Valid CreateStudentDto studentCourseDto) {
        return service.createStudent(studentCourseDto);
    }


    @PutMapping("/update")
    public ApiResponse<StudentDto> updateStudentAndCourses(
            @RequestBody @Valid UpdateStudentCourseDto updateStudentCourseDto ){
        return service.updateStudentAndCourses(updateStudentCourseDto);
    }

    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteStudentCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {

        ApiResponse<Void> response = service.deleteByStudentIdAndCourseId(studentId, courseId);

        return response;
    }



}
