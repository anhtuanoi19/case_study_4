package com.example.case_study_4.service.Impl;

import com.example.case_study_4.Mapper.CourseMapper;
import com.example.case_study_4.Mapper.StudentMapper;
import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.entity.Student;
import com.example.case_study_4.exception.AppException;
import com.example.case_study_4.exception.ErrorCode;
import com.example.case_study_4.repository.StudentRepository;
import com.example.case_study_4.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class StudentService implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ApiResponse<Page<StudentDto>> getAllPageable(Pageable pageable, Integer size, Integer currentPage, Locale locale) {
        Pageable pageableRequest = PageRequest.of(currentPage, size);
        Page<Student> studentPage = studentRepository.findAll(pageableRequest);
        Page<StudentDto> studentDtoPage = studentPage.map(StudentMapper.INSTANCE::toDto);
        ApiResponse<Page<StudentDto>> apiResponse = new ApiResponse<>();

        if (studentDtoPage != null && !studentDtoPage.isEmpty()) {
            apiResponse.setMessage(messageSource.getMessage("success.getAllPageable", null, locale));
            apiResponse.setResult(studentDtoPage);
        } else {
            throw new AppException(ErrorCode.LIST_STUDENT_NOT_FOUND);
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<StudentDto> create(StudentDto studentDto, Locale locale) {
        Student student = StudentMapper.INSTANCE.toEntity(studentDto);
        student = studentRepository.save(student);
        StudentDto savedStudentDto = StudentMapper.INSTANCE.toDto(student);
        ApiResponse<StudentDto> response = new ApiResponse<>();

        response.setMessage(messageSource.getMessage("success.create", null, locale));
        response.setResult(savedStudentDto);

        return response;
    }

    @Override
    public ApiResponse<StudentDto> findById(Long id, Locale locale) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        StudentDto studentDto = StudentMapper.INSTANCE.toDto(student);
        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("success.findById", null, locale));
        apiResponse.setResult(studentDto);

        return apiResponse;
    }

    @Override
    public ApiResponse<StudentDto> update(Long id, StudentDto studentDto, Locale locale) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setStatus(studentDto.getStatus());
        student = studentRepository.save(student);

        StudentDto updatedStudentDto = StudentMapper.INSTANCE.toDto(student);
        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("success.update", null, locale));
        apiResponse.setResult(updatedStudentDto);

        return apiResponse;
    }

    @Override
    public ApiResponse<Boolean> delete(Long id, Locale locale) {
        ApiResponse<Boolean> response = new ApiResponse<>();
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            response.setMessage(messageSource.getMessage("success.delete", null, locale));
            response.setResult(true);
        } else {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }
        return response;
    }

    @Transactional
    public ApiResponse<StudentDto> deleteMem(Long id, Locale locale) {
        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        if (student.getStatus() == 1) {
            student.setStatus(0);
            studentRepository.save(student);
            StudentDto studentDto = StudentMapper.INSTANCE.toDto(student);
            apiResponse.setMessage(messageSource.getMessage("success.soft.delete", null, locale));
            apiResponse.setResult(studentDto);
        } else {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }
        return apiResponse;
    }

    @Transactional
    public ApiResponse<StudentDto> open(Long id, Locale locale) {
        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        if (student.getStatus() == 0) {
            student.setStatus(1);
            studentRepository.save(student);
            StudentDto studentDto = StudentMapper.INSTANCE.toDto(student);
            apiResponse.setMessage(messageSource.getMessage("success.reopen", null, locale));
            apiResponse.setResult(studentDto);
        } else {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        return apiResponse;
    }

    @Transactional
    @Override
    public ApiResponse<Page<StudentDto>> getAllStudentsWithCourses(int page, int size, Locale locale) {
        ApiResponse<Page<StudentDto>> apiResponse = new ApiResponse<>();

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentPage = studentRepository.findAllWithCourses(pageable);

        Page<StudentDto> studentDetailDtos = studentPage.map(student -> {
            StudentDto studentDetailDto = StudentMapper.INSTANCE.toDto(student);
            List<CourseDto> courseDtos = student.getStudentCourses().stream()
                    .map(sc -> CourseMapper.INSTANCE.toDto(sc.getCourse()))
                    .collect(Collectors.toList());
            studentDetailDto.setCourses(courseDtos);
            return studentDetailDto;
        });

        apiResponse.setResult(studentDetailDtos);
        apiResponse.setMessage(messageSource.getMessage("success.getAllStudentsWithCourses", null, locale));
        return apiResponse;
    }

    @Transactional
    public ApiResponse<Void> deleteStudent(Long studentId, Locale locale) {
        // Xóa các bản ghi liên quan trong student_coure
        studentRepository.deleteByStudentId(studentId);

        // Xóa học sinh
        studentRepository.deleteById(studentId);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("success.delete", null, locale));
        return apiResponse;
    }




}
