package com.example.case_study_4.repository;

import com.example.case_study_4.entity.Course;
import com.example.case_study_4.entity.Student;
import com.example.case_study_4.entity.StudentCoure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c ORDER BY c.id DESC")
    Page<Course> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM StudentCoure sc WHERE sc.course.id = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT c FROM Course c WHERE c.id IN :courseIds")
    List<Course> findAllByIds(@Param("courseIds") List<Long> courseIds);

    @Query("SELECT sc FROM StudentCoure sc WHERE sc.course.id = :courseId")
    List<StudentCoure> findStudentCourseByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT c FROM Course c WHERE (:title IS NULL OR c.title LIKE %:title%)")
    Page<Course> searchByTitle(@Param("title") String title, Pageable pageable);
}
