package com.idp.cinema.repository;

import com.idp.cinema.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    void deleteCourseById(Long id);
    Course findCourseById(Long id);
}
