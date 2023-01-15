package com.cc.cinemaprocessor.repository;


import com.cc.cinemaprocessor.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    Course findByNameAndUniversityName(String name, String universityName);

}
