package com.example.case_study_4.Mapper;

import com.example.case_study_4.dto.request.CourseDto;
import com.example.case_study_4.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseDto toDto(Course course);
    Course toEntity(CourseDto courseDto);
    List<CourseDto> toListDto(List<Course> courseList);
    List<Course> toListEntity(List<CourseDto> courseList);
}
