package com.idp.cinema.service;

import com.idp.cinema.model.University;
import com.idp.cinema.model.Course;
import com.idp.cinema.model.Application;
import com.idp.cinema.repository.UniversityRepository;
import com.idp.cinema.repository.CourseRepository;
import com.idp.cinema.repository.ApplicationRepository;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UniversityService {


    private final UniversityRepository universityRepository;
    private final CourseRepository courseRepository;
    private final ApplicationRepository applicationRepository;


    public UniversityService(UniversityRepository universityRepository, CourseRepository courseRepository, ApplicationRepository applicationRepository) {
        this.universityRepository = universityRepository;
        this.courseRepository = courseRepository;
        this.applicationRepository = applicationRepository;
    }

    public List<University> getUniversities() {
        Iterable<University> cinemas = universityRepository.findAll();
        List<University> cinemasList = new ArrayList<>();
        cinemas.forEach(cinemasList::add);
        return cinemasList;
    }

    public University saveUniversity(University university) {
        return universityRepository.save(university);
    }

    public List<Course> getCourses(String universityName) {
        University university = universityRepository.findByNameEquals(universityName);
        return university.getCourses();
    }

    public Course saveCourse(Course course, String universityName) {
        University university = universityRepository.findByNameEquals(universityName);
        course.setUniversity(university);
        university.getCourses().add(course);

        Course savedCourse = courseRepository.save(course);
        universityRepository.save(university);
        return savedCourse;
    }

    public List<Application> getApplications(String username) {
        Iterable<Application> reservations = applicationRepository.findAllByUsername(username);
        List<Application> applicationList = new ArrayList<>();
        reservations.forEach(applicationList::add);
        return applicationList;
    }

    public Application saveApplication(Application application) {


        return applicationRepository.save(application);
    }

    @Transactional
    public void deleteApplication(Long id, String username) {
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (!optionalApplication.isPresent())
            throw new IllegalArgumentException("Wrong reservation selected");
        Application application = optionalApplication.get();
        if (!application.getUsername().equals(username))
            throw new IllegalArgumentException("Wrong reservation selected");

        applicationRepository.deleteByIdAndUsername(id, username);
    }

    @Transactional
    public void deleteCourse(Long id, String universityName){
        University university = universityRepository.findByNameEquals(universityName);
        Course course = courseRepository.findCourseById(id);
        university.getCourses().remove(course);
        universityRepository.save(university);

        courseRepository.deleteCourseById(id);
    }

    @Transactional
    public void deleteUniversity(Long id) {
        applicationRepository.deleteAllByUniversityName(universityRepository.findById(id).get().getName());
        universityRepository.deleteById(id);
    }

}

