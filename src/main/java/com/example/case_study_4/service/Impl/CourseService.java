package com.example.case_study_4.service.Impl;

import com.example.case_study_4.Mapper.CourseMapper;
import com.example.case_study_4.Mapper.StudentMapper;
import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.entity.Course;
import com.example.case_study_4.entity.Student;
import com.example.case_study_4.entity.StudentCoure;
import com.example.case_study_4.exception.AppException;
import com.example.case_study_4.exception.ErrorCode;
import com.example.case_study_4.repository.CourseRepository;
import com.example.case_study_4.repository.StudentCourseRepository;
import com.example.case_study_4.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CourseService implements ICourseService {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Override
    public ApiResponse<Page<CourseDto>> getAllPageable(Pageable pageable, Integer size, Integer currentPage) {
        Locale locale = LocaleContextHolder.getLocale();

        Pageable pageableRequest = PageRequest.of(currentPage, size);
        Page<Course> coursePage = courseRepository.findAll(pageableRequest);
        Page<CourseDto> courseDtoPage = coursePage.map(CourseMapper.INSTANCE::toDto);
        ApiResponse<Page<CourseDto>> apiResponse = new ApiResponse<>();

        if (courseDtoPage != null){
            apiResponse.setMessage(messageSource.getMessage("success.getAllCourses", null, locale));
            apiResponse.setResult(courseDtoPage);
        } else {
            throw new AppException(ErrorCode.LIST_STUDENT_NOT_FOUND);
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<CourseDto> create(CourseDto courseDto) {
        Locale locale = LocaleContextHolder.getLocale();

        Course course = CourseMapper.INSTANCE.toEntity(courseDto);
        course = courseRepository.save(course);
        CourseDto savedCourseDto = CourseMapper.INSTANCE.toDto(course);
        ApiResponse<CourseDto> response = new ApiResponse<>();

        response.setMessage(messageSource.getMessage("success.create.course", null, locale));
        response.setResult(savedCourseDto);

        return response;
    }

    @Override
    public ApiResponse<CourseDto> findById(Long id) {
        Locale locale = LocaleContextHolder.getLocale();

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        CourseDto courseDto = CourseMapper.INSTANCE.toDto(course);
        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("success.getCourse", null, locale));
        apiResponse.setResult(courseDto);

        return apiResponse;
    }

    @Override
    public ApiResponse<CourseDto> update(Long id, CourseDto courseDto) {
        Locale locale = LocaleContextHolder.getLocale();

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        course.setId(id);
        course = CourseMapper.INSTANCE.toEntity(courseDto);
        courseRepository.save(course);

        CourseDto courseDto1 = CourseMapper.INSTANCE.toDto(course);
        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("success.update", null, locale));
        apiResponse.setResult(courseDto1);

        return apiResponse;
    }

    @Override
    public ApiResponse<Boolean> delete(Long id) {
        Locale locale = LocaleContextHolder.getLocale();

        ApiResponse<Boolean> response = new ApiResponse<>();
        if (courseRepository.existsById(id)) {
            courseRepository.deleteByCourseId(id);
            courseRepository.deleteById(id);
            response.setMessage(messageSource.getMessage("success.delete", null, locale));
            response.setResult(true);
        } else {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        return response;
    }

    @Override
    @Transactional
    public ApiResponse<Boolean> deleteMem(Long courseId) {
        Locale locale = LocaleContextHolder.getLocale();

        ApiResponse<Boolean> response = new ApiResponse<>();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        if (course.getId() == 1){
            course.setStatus(0);
            courseRepository.save(course);

            List<StudentCoure> studentCourses = courseRepository.findStudentCourseByCourseId(courseId);

            studentCourses.forEach(sc -> sc.setStatus(0));
            studentCourseRepository.saveAll(studentCourses);

            // Tạo phản hồi API
            response.setMessage(messageSource.getMessage("success.softDeleteCourse", null, locale));
            response.setResult(true);
        }else {
            throw new AppException(ErrorCode.ALREADY_DELETED);
        }

        return response;
    }

    @Override
    public ApiResponse<CourseDto> open(Long id) {
        Locale locale = LocaleContextHolder.getLocale();

        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        if (course.getStatus() == 0){
            course.setStatus(1);
            courseRepository.save(course);
            CourseDto courseDto = CourseMapper.INSTANCE.toDto(course);
            apiResponse.setMessage(messageSource.getMessage("success.reopen", null, locale));
            apiResponse.setResult(courseDto);
        } else {
            throw new AppException(ErrorCode.ALREADY_DELETED);
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<Page<CourseDto>> findByTitle(String title, int page, int size) {
        Locale locale = LocaleContextHolder.getLocale();
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursePage = courseRepository.searchByTitle(title, pageable);

        Page<CourseDto> courseDtoPage = coursePage.map(course -> CourseMapper.INSTANCE.toDto(course));

        ApiResponse<Page<CourseDto>> apiResponse = new ApiResponse<>();
        if (coursePage.hasContent()) {
            apiResponse.setMessage(messageSource.getMessage("success.searchCourses", null, locale));
        } else {
            apiResponse.setMessage(messageSource.getMessage("info.noCoursesFound", null, locale));
        }
        apiResponse.setResult(courseDtoPage);

        return apiResponse;
    }



}
