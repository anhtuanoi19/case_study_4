package com.example.case_study_4.service.Impl;

import com.example.case_study_4.Mapper.CourseMapper;
import com.example.case_study_4.Mapper.StudentMapper;
import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.entity.Course;
import com.example.case_study_4.entity.Student;
import com.example.case_study_4.exception.AppException;
import com.example.case_study_4.exception.ErrorCode;
import com.example.case_study_4.repository.CourseRepository;
import com.example.case_study_4.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseService implements ICourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public ApiResponse<Page<CourseDto>> getAllPageable(Pageable pageable, Integer size, Integer currentPage) {
        Pageable pageableRequest = PageRequest.of(currentPage, size);
        Page<Course> coursePage = courseRepository.findAll(pageableRequest);
        Page<CourseDto> courseDtoPage = coursePage.map(CourseMapper.INSTANCE::toDto);
        ApiResponse<Page<CourseDto>> apiResponse = new ApiResponse<>();

        if (courseDtoPage != null){
            apiResponse.setMessage("Lấy danh sách khóa học thành công");
            apiResponse.setResult(courseDtoPage);
        }else {
            throw new AppException(ErrorCode.LIST_STUDENT_NOT_FOUND);
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<CourseDto> create(CourseDto courseDto) {
        Course course = CourseMapper.INSTANCE.toEntity(courseDto);
        course = courseRepository.save(course);
        CourseDto savedCourseDto = CourseMapper.INSTANCE.toDto(course);
        ApiResponse<CourseDto> response = new ApiResponse<>();

        response.setMessage("Created");
        response.setResult(savedCourseDto);

        return response;
    }

    @Override
    public ApiResponse<CourseDto> findById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        CourseDto courseDto = CourseMapper.INSTANCE.toDto(course);
        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Lấy course thành công");
        apiResponse.setResult(courseDto);

        return apiResponse;
    }

    @Override
    public ApiResponse<CourseDto> update(Long id, CourseDto courseDto) {
        Course course = courseRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        course.setId(id);
        course = CourseMapper.INSTANCE.toEntity(courseDto);
        courseRepository.save(course);

        CourseDto courseDto1 = CourseMapper.INSTANCE.toDto(course);
        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("update thành công");
        apiResponse.setResult(courseDto1);

        return apiResponse;
    }

    @Override
    public ApiResponse<Boolean> delete(Long id) {
        ApiResponse<Boolean> response = new ApiResponse<>();
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            response.setMessage("Xóa thành công");
            response.setResult(true);
        } else {
            response.setMessage("Không tìm thấy course");
            response.setResult(false);
        }
        return response;
    }

    public ApiResponse<CourseDto> deleteMem(Long id) {
        ApiResponse apiResponse = new ApiResponse<>();
        Course course = courseRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        if (course.getStatus() == 1){
            course.setStatus(0);
            course.setId(id);
            courseRepository.save(course);
            CourseDto courseDto = CourseMapper.INSTANCE.toDto(course);
            if (courseDto != null){
                apiResponse.setMessage("Xoa thành công");
                apiResponse.setResult(courseDto);
            }else {
                throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
            }
        }else {
            apiResponse.setMessage("Học sinh này không tồn tại");
        }
        return apiResponse;
    }

    public ApiResponse<CourseDto> open(Long id) {
        ApiResponse apiResponse = new ApiResponse<>();
        Course course = courseRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        if (course.getStatus() == 0){
            course.setStatus(1);
            course.setId(id);
            courseRepository.save(course);
            CourseDto courseDto = CourseMapper.INSTANCE.toDto(course);
            if (courseDto != null){
                apiResponse.setMessage("Mở thành công");
                apiResponse.setResult(courseDto);
            }else {
                throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
            }
        }else {
            apiResponse.setMessage("Học sinh này không tồn tại");
        }

        return apiResponse;
    }
}
