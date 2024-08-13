package com.example.case_study_4.service;

import com.example.case_study_4.dto.request.*;
import com.example.case_study_4.dto.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Locale;

public interface IStudentCourseService {
    ApiResponse<Page<GetAllDto>> getAll(Pageable pageable);
    ApiResponse<Page<GetAllDto>> searchByStudentName(String name, Pageable pageable) ;
}
