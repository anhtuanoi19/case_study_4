package com.example.case_study_4.controller;

import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.service.IStudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private IStudentService service;

    @GetMapping()
    public ApiResponse<Page<StudentDto>> getAllStudentsPageable(
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "page", defaultValue = "0") Integer page) {

        Pageable pageable = PageRequest.of(page, size); // Tạo Pageable từ size và currentPage
        return service.getAllPageable(pageable, size, page);
    }

    @PostMapping()
    ApiResponse<StudentDto> create(@RequestBody @Valid StudentDto studentDto){
        return service.create(studentDto);
    }

    @GetMapping("/findById/{id}")
    ApiResponse<StudentDto> findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PutMapping("/update/{id}")
    ApiResponse<StudentDto> update(@RequestBody @Valid StudentDto studentDto, @PathVariable Long id){
        return service.update(id, studentDto);
    }
    @DeleteMapping("delete/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @PostMapping("/xoa-mem/{id}")
    public ApiResponse<StudentDto> deleteMem(@PathVariable Long id){
        return service.deleteMem(id);
    }

    @PostMapping("/open/{id}")
    public ApiResponse<StudentDto> open(@PathVariable Long id){
        return service.open(id);
    }


    @GetMapping("/all")
    public ApiResponse<Page<StudentDto>> getAllStudentsWithCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ApiResponse<Page<StudentDto>> response = service.getAllStudentsWithCourses(page, size);
        return response;
    }

    @DeleteMapping("/deleteStudent/{studentId}")
    public ApiResponse<Void> deleteStudent(@PathVariable Long studentId) {
        return service.deleteStudent(studentId);
    }



}
