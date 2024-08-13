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
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Override
    public ApiResponse<Page<GetAllDto>> getAll(int page, int size, Locale locale) {
        Pageable pageable = PageRequest.of(page, size);
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
    public ApiResponse<Page<GetAllDto>> searchByStudentName(String name, int page, int size, Locale locale) {
        Pageable pageable = PageRequest.of(page, size);

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

    @Transactional
    public ApiResponse<StudentCourseDto> createAll(StudentCourseDto studentCourseDto, Locale locale) {
        ApiResponse<StudentCourseDto> apiResponse = new ApiResponse<>();
        Set<StudentCoure> studentCoureSet = new HashSet<>();

        for (StudentDto studentDto : studentCourseDto.getStudentDtoList()) {
            if (studentRepository.existsByEmail(studentDto.getEmail())) {
                throw new AppException(ErrorCode.STUDENT_EMAIL_EXISTS);
            }
            Student student;
            if (studentDto.getId() != null) {
                student = studentRepository.findById(studentDto.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
            } else {
                student = StudentMapper.INSTANCE.toEntity(studentDto);
                student = studentRepository.save(student);
            }

            for (CourseDto courseDto : studentCourseDto.getCourseDtoList()) {
                Course course;
                if (courseDto.getId() != null) {
                    course = courseRepository.findById(courseDto.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
                } else {
                    course = CourseMapper.INSTANCE.toEntity(courseDto);
                    course = courseRepository.save(course);
                }

                StudentCoure studentCoure = new StudentCoure();
                studentCoure.setStudent(student);
                studentCoure.setCourse(course);
                studentCoure.setStatus(1);
                studentCoureSet.add(studentCourseRepository.save(studentCoure));
            }
        }

        StudentCourseDto responseDto = new StudentCourseDto();
        responseDto.setStudentDtoList(studentCourseDto.getStudentDtoList());
        responseDto.setCourseDtoList(studentCourseDto.getCourseDtoList());

        apiResponse.setResult(responseDto);
        apiResponse.setMessage(messageSource.getMessage("success.create", null, locale));
        return apiResponse;
    }

    @Transactional
    public ApiResponse<StudentDto> updateStudentAndCourses(UpdateStudentCourseDto dto, Locale locale) {
        Student student = studentRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setStatus(dto.getStatus());
        studentRepository.save(student);

        List<Long> currentCourseIds = studentCourseRepository.findCourseIdsByStudentId1(student.getId());

        Set<Long> newCourseIds = new HashSet<>(dto.getCourseIds());

        Set<Long> coursesToDeactivate = currentCourseIds.stream()
                .filter(id -> !newCourseIds.contains(id))
                .collect(Collectors.toSet());

        if (!coursesToDeactivate.isEmpty()) {
            studentCourseRepository.updateStatusByStudentIdAndCourseIds(student.getId(), coursesToDeactivate);
        }

        List<Course> courses = courseRepository.findAllByIds(dto.getCourseIds());

        for (Course course : courses) {
            if (course.getStatus() == 0) {
                throw new AppException(ErrorCode.COURSE_CLOSE);
            }
        }

        Set<StudentCoure> newStudentCourses = courses.stream()
                .map(course -> {
                    StudentCoure studentCourse = studentCourseRepository.findByStudentIdAndCourseId(student.getId(), course.getId())
                            .orElseGet(() -> new StudentCoure());
                    studentCourse.setStudent(student);
                    studentCourse.setCourse(course);
                    studentCourse.setStatus(dto.getStatus());
                    return studentCourse;
                })
                .collect(Collectors.toSet());

        studentCourseRepository.saveAll(newStudentCourses);

        // Tạo phản hồi API
        StudentDto updatedStudentDto = StudentMapper.INSTANCE.toDto(student);
        ApiResponse<StudentDto> response = new ApiResponse<>();
        response.setResult(updatedStudentDto);
        response.setMessage(messageSource.getMessage("success.update", null, locale));

        return response;
    }



}
