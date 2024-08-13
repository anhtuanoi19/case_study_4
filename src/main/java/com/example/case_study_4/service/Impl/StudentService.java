package com.example.case_study_4.service.Impl;

import com.example.case_study_4.Mapper.CourseMapper;
import com.example.case_study_4.Mapper.StudentMapper;
import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.CreateStudentDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.dto.request.UpdateStudentCourseDto;
import com.example.case_study_4.dto.response.ApiResponse;
import com.example.case_study_4.entity.Course;
import com.example.case_study_4.entity.Student;
import com.example.case_study_4.entity.StudentCoure;
import com.example.case_study_4.exception.AppException;
import com.example.case_study_4.exception.ErrorCode;
import com.example.case_study_4.repository.CourseRepository;
import com.example.case_study_4.repository.StudentCourseRepository;
import com.example.case_study_4.repository.StudentRepository;
import com.example.case_study_4.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private StudentCourseRepository studentCourseRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public ApiResponse<StudentDto> findById(Long id) {
        Locale locale = LocaleContextHolder.getLocale();

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        StudentDto studentDto = StudentMapper.INSTANCE.toDto(student);
        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("success.findById", null, locale));
        apiResponse.setResult(studentDto);

        return apiResponse;
    }


    @Transactional
    public ApiResponse<StudentDto> deleteMem(Long id) {
        Locale locale = LocaleContextHolder.getLocale();

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
    public ApiResponse<StudentDto> open(Long id) {
        Locale locale = LocaleContextHolder.getLocale();

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
    public ApiResponse<Void> deleteStudent(Long studentId) {
        Locale locale = LocaleContextHolder.getLocale();

        // Xóa các bản ghi liên quan trong student_coure
        studentRepository.deleteByStudentId(studentId);

        // Xóa học sinh
        studentRepository.deleteById(studentId);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("success.delete", null, locale));
        return apiResponse;
    }

    @Override
    @Transactional
    public ApiResponse<CreateStudentDto> createStudent(CreateStudentDto createStudentDto) {
        Locale locale = LocaleContextHolder.getLocale();

        // Tạo mới đối tượng sinh viên từ DTO
        Student student = StudentMapper.INSTANCE.toStudent(createStudentDto);
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new AppException(ErrorCode.STUDENT_EMAIL_EXISTS);
        }
        if (student.getName() != null && !student.getName().matches("^[a-zA-Z\\s]*$")) {
            throw new AppException(ErrorCode.INVALID_NAME);
        }

        // Kiểm tra nếu tên quá dài
        if (student.getName() != null && student.getName().length() > 50) {
            throw new AppException(ErrorCode.NAME_TOO_LONG);
        }
        student = studentRepository.save(student);

        // Kiểm tra nếu danh sách khóa học không có thì chỉ trả về sinh viên đã tạo
        if (createStudentDto.getCourses() == null || createStudentDto.getCourses().isEmpty()) {
            CreateStudentDto savedStudentDto = StudentMapper.INSTANCE.toCreateStudentDto(student);
            ApiResponse<CreateStudentDto> response = new ApiResponse<>();
            response.setResult(savedStudentDto);
            response.setMessage(messageSource.getMessage("success.create", null, locale));
            return response;
        }

        // Lấy danh sách ID khóa học từ DTO
        List<Long> courseIds = createStudentDto.getCourses().stream()
                .map(CourseDto::getId)
                .collect(Collectors.toList());

        // Tìm tất cả các khóa học có ID từ danh sách
        List<Course> existingCourses = courseRepository.findAllByIds(courseIds);

        // Tạo mới các khóa học không tồn tại
        Set<Long> existingCourseIds = existingCourses.stream()
                .map(Course::getId)
                .collect(Collectors.toSet());

        List<Course> newCourses = createStudentDto.getCourses().stream()
                .filter(courseDto -> courseDto.getId() == null || !existingCourseIds.contains(courseDto.getId()))
                .map(courseDto -> {
                    Course newCourse = CourseMapper.INSTANCE.toEntity(courseDto);
                    return courseRepository.save(newCourse);
                })
                .collect(Collectors.toList());

        // Kết hợp các khóa học đã tồn tại và khóa học mới
        List<Course> allCourses = new ArrayList<>(existingCourses);
        allCourses.addAll(newCourses);

        // Tạo và lưu các bản ghi vào bảng phụ StudentCoure
        List<StudentCoure> studentCourses = new ArrayList<>();
        for (Course course : allCourses) {
            StudentCoure studentCourse = new StudentCoure();
            studentCourse.setStudent(student);
            studentCourse.setCourse(course);
            studentCourse.setStatus(1); // Hoặc trạng thái mặc định theo yêu cầu
            studentCourses.add(studentCourse);
        }
        studentCourseRepository.saveAll(studentCourses);

        // Tạo DTO để trả về
        CreateStudentDto savedStudentDto = StudentMapper.INSTANCE.toCreateStudentDto(student);
        ApiResponse<CreateStudentDto> response = new ApiResponse<>();
        response.setResult(savedStudentDto);
        response.setMessage(messageSource.getMessage("success.create", null, locale));

        return response;
    }

    @Transactional
    @Override
    public ApiResponse<Void> deleteByStudentIdAndCourseId(Long studentId, Long courseId) {
        // Lấy locale từ LocaleContextHolder
        Locale locale = LocaleContextHolder.getLocale();

        // Kiểm tra sự tồn tại của bản ghi
        boolean exists = studentCourseRepository.existsByStudentIdAndCourseId(studentId, courseId);

        ApiResponse<Void> apiResponse = new ApiResponse<>();

        if (exists) {
            // Xóa bản ghi
            studentCourseRepository.deleteByStudentIdAndCourseId(studentId, courseId);
            apiResponse.setMessage(messageSource.getMessage("success.deleteStudentCourse", null, locale));
        } else {
            // Không tìm thấy bản ghi
            apiResponse.setMessage(messageSource.getMessage("error.studentCourseNotFound", null, locale));
        }

        return apiResponse;
    }

    @Transactional
    public ApiResponse<StudentDto> updateStudentAndCourses(UpdateStudentCourseDto dto) {
        Locale locale = LocaleContextHolder.getLocale();

        Student student = studentRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        if (dto.getName() != null && !dto.getName().matches("^[a-zA-Z\\s]*$")) {
            throw new AppException(ErrorCode.INVALID_NAME);
        }

        // Kiểm tra nếu tên quá dài
        if (dto.getName() != null && dto.getName().length() > 50) {
            throw new AppException(ErrorCode.NAME_TOO_LONG);
        }
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setStatus(dto.getStatus());
        studentRepository.save(student);

        List<Long> currentCourseIds = studentCourseRepository.findCourseIdsByStudentId1(student.getId());

        Set<Long> newCourseIds = new HashSet<>(dto.getCourseIds());

        // Xử lý khóa học mới
        if (dto.getListCourse() != null && !dto.getListCourse().isEmpty()) {
            // Tạo danh sách các khóa học mới
            List<Course> coursesToSave = dto.getListCourse().stream()
                    .filter(courseDto -> courseDto.getId() == null) // Chỉ xử lý khóa học chưa có id (mới)
                    .map(courseDto -> {
                        Course newCourse = new Course();
                        newCourse.setTitle(courseDto.getTitle());
                        newCourse.setDescription(courseDto.getDescription());
                        newCourse.setStatus(courseDto.getStatus());
                        return newCourse;
                    })
                    .collect(Collectors.toList());

            // Lưu tất cả các khóa học mới cùng một lúc
            List<Course> savedCourses = courseRepository.saveAll(coursesToSave);

            // Cập nhật danh sách newCourseIds với các khóa học mới
            savedCourses.forEach(course -> newCourseIds.add(course.getId()));
        }

        Set<Long> coursesToDeactivate = currentCourseIds.stream()
                .filter(id -> !newCourseIds.contains(id))
                .collect(Collectors.toSet());

        if (!coursesToDeactivate.isEmpty()) {
            studentCourseRepository.updateStatusByStudentIdAndCourseIds(student.getId(), coursesToDeactivate);
        }

        List<Course> courses = courseRepository.findAllByIds(new ArrayList<>(newCourseIds));

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

    @Override
    public ApiResponse<Page<StudentDto>> search(String name, Pageable pageable) {
        Locale locale = LocaleContextHolder.getLocale();

        // Kiểm tra nếu tên không phải là chữ cái (chỉ cho phép chữ cái và có thể bao gồm khoảng trắng)
        if (name != null && !name.matches("^[a-zA-Z\\s]*$")) {
            throw new AppException(ErrorCode.INVALID_NAME);
        }

        // Kiểm tra nếu tên quá dài
        if (name != null && name.length() > 50) {
            throw new AppException(ErrorCode.NAME_TOO_LONG);
        }

        // Tìm kiếm sinh viên theo tên và phân trang
        Page<Student> studentPage = studentRepository.findByNameContaining(name, pageable);

        if (studentPage.isEmpty()) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        // Chuyển đổi từ Page<Student> sang Page<StudentDto>
        Page<StudentDto> studentDtoPage = studentPage.map(student -> {
            StudentDto studentDto = StudentMapper.INSTANCE.toDto(student);

            // Map danh sách courses hoặc để rỗng nếu không có
            List<CourseDto> courseDtos = student.getStudentCourses() != null
                    ? student.getStudentCourses().stream()
                    .map(sc -> CourseMapper.INSTANCE.toDto(sc.getCourse()))
                    .collect(Collectors.toList())
                    : Collections.emptyList();

            studentDto.setCourses(courseDtos);
            return studentDto;
        });

        // Tạo phản hồi API
        ApiResponse<Page<StudentDto>> response = new ApiResponse<>();
        response.setResult(studentDtoPage);
        response.setMessage(messageSource.getMessage("success.search", null, locale));

        return response;
    }

}


