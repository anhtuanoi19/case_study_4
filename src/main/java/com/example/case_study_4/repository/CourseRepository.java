package com.example.case_study_4.repository;

import com.example.case_study_4.entity.Course;
import com.example.case_study_4.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM StudentCoure sc WHERE sc.course.id = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);
}
