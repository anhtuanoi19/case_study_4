package com.example.case_study_4.Mapper;

import com.example.case_study_4.dto.request.StudentDto;
import com.example.case_study_4.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "courses", ignore = true)
    StudentDto toDto(Student student);
    @Mapping(target = "studentCourses", ignore = true) // Nếu không muốn ánh xạ trường này
    @Mapping(target = "id", source = "id")

    Student toEntity(StudentDto studentDto);
    List<StudentDto> toListDto(List<Student> studentList);
    List<Student> toListEntity(List<Student> studentList);

}
