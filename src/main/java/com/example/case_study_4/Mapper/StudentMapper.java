package com.example.case_study_4.Mapper;

import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.dto.request.CreateStudentDto;
import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.entity.Student;
import com.example.case_study_4.entity.StudentCoure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);
    @Mapping(target = "courses", expression = "java(mapCourses(student.getStudentCourses()))")
    StudentDto toDto(Student student);
    Student toEntity(StudentDto studentDto);
    CreateStudentDto toCreateStudentDto(Student student);
    Student toStudent(CreateStudentDto studentDto);

    default List<CourseDto> mapCourses(Set<StudentCoure> studentCourses) {
        if (studentCourses == null) {
            return Collections.emptyList(); // Trả về danh sách rỗng nếu studentCourses là null
        }
        return studentCourses.stream()
                .map(studentCoure -> CourseMapper.INSTANCE.toDto(studentCoure.getCourse()))
                .collect(Collectors.toList());
    }
}
