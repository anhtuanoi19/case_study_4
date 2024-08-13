package com.example.case_study_4.Mapper;

import com.example.case_study_4.dto.request.StudentCourseDto;
import com.example.case_study_4.entity.StudentCoure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {StudentMapper.class, CourseMapper.class})
public interface StudenCourseMapper {
    StudenCourseMapper INSTANCE = Mappers.getMapper(StudenCourseMapper.class);
    StudentCourseDto toDto(StudentCoure studentCoure);
    StudentCoure toEntity(StudentCourseDto studentCourseDto);
}
