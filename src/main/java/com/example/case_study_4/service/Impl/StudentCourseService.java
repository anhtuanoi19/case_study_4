package com.example.case_study_4.service.Impl;

import com.example.case_study_4.Mapper.CourseMapper;
import com.example.case_study_4.Mapper.StudentMapper;
import com.example.case_study_4.dto.request.*;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.entity.Course;
import com.example.case_study_4.entity.Student;
import com.example.case_study_4.entity.StudentCoure;
import com.example.case_study_4.exception.AppException;
import com.example.case_study_4.exception.ErrorCode;
import com.example.case_study_4.repository.CourseRepository;
import com.example.case_study_4.repository.StudentCourseRepository;
import com.example.case_study_4.repository.StudentRepository;
import com.example.case_study_4.service.IStudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentCourseService implements IStudentCourseService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Override
    public ApiResponse<Page<GetAllDto>> getAll(Pageable pageable) {
        Locale locale = LocaleContextHolder.getLocale();

        Page<Object[]> resultsPage = studentCourseRepository.getAllByConCat(pageable);
        List<GetAllDto> dtoList = new ArrayList<>();

        for (Object[] result : resultsPage.getContent()) {
            String studentName = (String) result[0];
            String studentEmail = (String) result[1];
            String courseTitles = (String) result[2];

            dtoList.add(new GetAllDto(studentName, studentEmail, courseTitles));
        }

        Page<GetAllDto> dtoPage = new PageImpl<>(dtoList, pageable, resultsPage.getTotalElements());

        ApiResponse<Page<GetAllDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(dtoPage);
        apiResponse.setMessage(messageSource.getMessage("success.get.all", null, locale));

        return apiResponse;
    }

    @Override
    public ApiResponse<Page<GetAllDto>> searchByStudentName(String name, Pageable pageable) {
        Locale locale = LocaleContextHolder.getLocale();


        ApiResponse<Page<GetAllDto>> apiResponse = new ApiResponse<>();
        Page<Object[]> resultsPage = studentCourseRepository.searchByStudentName(name, pageable);

        if (resultsPage.hasContent()) {
            List<GetAllDto> dtoList = new ArrayList<>();
            for (Object[] result : resultsPage.getContent()) {
                String studentName = (String) result[0];
                String studentEmail = (String) result[1];
                String courseTitles = (String) result[2];

                dtoList.add(new GetAllDto(studentName, studentEmail, courseTitles));
            }

            Page<GetAllDto> dtoPage = new PageImpl<>(dtoList, pageable, resultsPage.getTotalElements());

            apiResponse.setResult(dtoPage);
            apiResponse.setMessage(messageSource.getMessage("success.search", null, locale));
        } else {
            apiResponse.setMessage(messageSource.getMessage("error.student.not.found", null, locale));
        }

        return apiResponse;
    }





}
