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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCoure, Long> {
        Optional<StudentCoure> findByStudentAndCourse(Student student, Course course);

        @Query(value = "SELECT s.name AS studentName, " +
                "       s.email AS studentEmail, " +
                "       GROUP_CONCAT(c.title ORDER BY c.title ASC SEPARATOR ', ') AS courseTitles " +
                " FROM student_coure sc " +
                " JOIN student s ON sc.student_id = s.id " +
                " JOIN course c ON sc.course_id = c.id " +
                " GROUP BY s.id, s.name, s.email",
                countQuery = "SELECT COUNT(DISTINCT s.id) FROM student_coure sc " +
                        " JOIN student s ON sc.student_id = s.id " +
                        " JOIN course c ON sc.course_id = c.id",
                nativeQuery = true)
        Page<Object[]> getAllByConCat(Pageable pageable);

        @Query(value = "SELECT s.name AS studentName, " +
                "       s.email AS studentEmail, " +
                "       GROUP_CONCAT(c.title ORDER BY c.title ASC SEPARATOR ', ') AS courseTitles " +
                " FROM student_coure sc " +
                " JOIN student s ON sc.student_id = s.id " +
                " JOIN course c ON sc.course_id = c.id " +
                " WHERE (:name IS NULL OR s.name LIKE %:name%) " +
                " GROUP BY s.id, s.name, s.email",
                countQuery = "SELECT COUNT(DISTINCT s.id) " +
                        " FROM student_coure sc " +
                        " JOIN student s ON sc.student_id = s.id " +
                        " JOIN course c ON sc.course_id = c.id",
                nativeQuery = true)
        Page<Object[]> searchByStudentName(String name, Pageable pageable);


        @Modifying
        @Query("DELETE FROM StudentCoure sc WHERE sc.student.id = :studentId")
        void deleteByStudentId(Long studentId);

        @Query("SELECT sc FROM StudentCoure sc WHERE sc.student.id = :studentId AND sc.course.id = :courseId")
        Optional<StudentCoure> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

        Optional<StudentCoure> findByStudentId(Long studentId);
        boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

        @Query("SELECT sc.course.id FROM StudentCoure sc WHERE sc.student.id = :studentId")
        Set<Long> findCourseIdsByStudentId(@Param("studentId") Long studentId);

        @Modifying
        @Query("UPDATE StudentCoure sc SET sc.status = :status WHERE sc.student.id = :studentId AND sc.course.id = :courseId")
        void updateStatusByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("status") int status);



}

