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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public ApiResponse<Page<StudentDto>> getAllPageable(Pageable pageable, Integer size, Integer currentPage) {
        Pageable pageableRequest = PageRequest.of(currentPage, size); // Tạo Pageable mới với size và currentPage
        Page<Student> studentPage = studentRepository.findAll(pageableRequest);
        Page<StudentDto> studentDtoPage = studentPage.map(StudentMapper.INSTANCE::toDto);
        ApiResponse<Page<StudentDto>> apiResponse = new ApiResponse<>();

        if (studentDtoPage != null){
            apiResponse.setMessage("Lấy danh sách học sinh thành công");
            apiResponse.setResult(studentDtoPage);
        }else {
            throw new AppException(ErrorCode.LIST_STUDENT_NOT_FOUND);
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<StudentDto> create(StudentDto studentDto) {
        Student student = StudentMapper.INSTANCE.toEntity(studentDto);
        student = studentRepository.save(student);
        StudentDto savedStudentDto = StudentMapper.INSTANCE.toDto(student);
        ApiResponse<StudentDto> response = new ApiResponse<>();
    
        response.setMessage("Created");
        response.setResult(savedStudentDto);

        return response;
    }

    @Override
    public ApiResponse<StudentDto> findById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        StudentDto studentDto = StudentMapper.INSTANCE.toDto(student);
        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Lấy student thành công");
        apiResponse.setResult(studentDto);

        return apiResponse;
    }

    @Override
    public ApiResponse<StudentDto> update(Long id, StudentDto studentDto) {
        Student student = studentRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        student.setId(id);
        student = StudentMapper.INSTANCE.toEntity(studentDto);
        studentRepository.save(student);

        StudentDto studentDto1 = StudentMapper.INSTANCE.toDto(student);
        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("update thành công");
        apiResponse.setResult(studentDto1);

        return apiResponse;
    }

    @Override
    public ApiResponse<Boolean> delete(Long id) {
        ApiResponse<Boolean> response = new ApiResponse<>();
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            response.setMessage("Xóa thành công");
            response.setResult(true);
        } else {
            response.setMessage("Không tìm thấy student");
            response.setResult(false);
        }
        return response;
    }

    public ApiResponse<StudentDto> deleteMem(Long id) {
        ApiResponse apiResponse = new ApiResponse<>();
        Student student = studentRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        if (student.getStatus() == 1){
            student.setStatus(0);
            student.setId(id);
            studentRepository.save(student);
            StudentDto studentDto = StudentMapper.INSTANCE.toDto(student);
            if (studentDto != null){
                apiResponse.setMessage("Xoa thành công");
                apiResponse.setResult(studentDto);
            }else {
                throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
            }
        }else {
            apiResponse.setMessage("Học sinh này không tồn tại");
        }
        return apiResponse;
    }

    public ApiResponse<StudentDto> open(Long id) {
        ApiResponse apiResponse = new ApiResponse<>();
        Student student = studentRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        if (student.getStatus() == 0){
            student.setStatus(1);
            student.setId(id);
            studentRepository.save(student);
            StudentDto studentDto = StudentMapper.INSTANCE.toDto(student);
            if (studentDto != null){
                apiResponse.setMessage("Mở thành công");
                apiResponse.setResult(studentDto);
            }else {
                throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
            }
        }else {
            apiResponse.setMessage("Học sinh này không tồn tại");
        }

        return apiResponse;
    }

    //select 1-n
    @Transactional
    public ApiResponse<Page<StudentDto>> getAllStudentsWithCourses(int page, int size) {
        ApiResponse<Page<StudentDto>> apiResponse = new ApiResponse<>();

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentPage = studentRepository.findAllWithCourses(pageable);

        Page<StudentDto> studentDetailDtos = studentPage.map(student -> {
            StudentDto studentDetailDto = new StudentDto();
            studentDetailDto.setId(student.getId());
            studentDetailDto.setName(student.getName());
            studentDetailDto.setEmail(student.getEmail());
            studentDetailDto.setStatus(student.getStatus());

            List<CourseDto> courseDtos = student.getStudentCourses().stream()
                    .map(sc -> CourseMapper.INSTANCE.toDto(sc.getCourse()))
                    .collect(Collectors.toList());
            studentDetailDto.setCourses(courseDtos);

            return studentDetailDto;
        });

        apiResponse.setResult(studentDetailDtos);
        apiResponse.setMessage("Lấy danh sách học sinh thành công");
        return apiResponse;
    }

    @Transactional
    public ApiResponse<Void> deleteStudent(Long studentId) {
        // Xóa các bản ghi liên quan trong student_coure
        studentRepository.deleteByStudentId(studentId);

        // Xóa học sinh
        studentRepository.deleteById(studentId);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Xóa học sinh thành công");
        return apiResponse;
    }






}
