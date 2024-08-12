package com.example.case_study_4.repository;

import com.example.case_study_4.entity.Student;
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
public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findAll(Pageable pageable);
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.studentCourses sc LEFT JOIN FETCH sc.course")
    Page<Student> findAllWithCourses(Pageable pageable);
    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM StudentCoure sc WHERE sc.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);
}
