package com.cc.cinemaprocessor.service;

import com.cc.cinemaprocessor.model.University;
import com.cc.cinemaprocessor.model.Course;
import com.cc.cinemaprocessor.model.Application;
import com.cc.cinemaprocessor.repository.UniversityRepository;
import com.cc.cinemaprocessor.repository.CourseRepository;
import com.cc.cinemaprocessor.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    public List<University> getCinemas() {
        Iterable<University> cinemas = universityRepository.findAll();
        List<University> cinemasList = new ArrayList<>();
        cinemas.forEach(cinemasList::add);
        return cinemasList;
    }

    public University saveCinema(University university) {
        return universityRepository.save(university);
    }

    public List<Course> getFilms(String cinemaName) {
        University university = universityRepository.findByNameEquals(cinemaName);
        return university.getCourses();
    }

    @Transactional
    public Course saveFilm(Course course, String cinemaName) {
        University university = universityRepository.findByNameEquals(cinemaName);
        course.setUniversity(university);
        university.getCourses().add(course);

        Course savedCourse = courseRepository.save(course);
        universityRepository.save(university);
        return savedCourse;
    }

    public List<Application> getReservations(String username) {
        Iterable<Application> reservations = applicationRepository.findAllByUsername(username);
        List<Application> applicationList = new ArrayList<>();
        reservations.forEach(applicationList::add);
        return applicationList;
    }

    public Application saveApplication(Application application) {

        return applicationRepository.save(application);
    }

    @Transactional
    public void deleteReservation(Long id, String username) {
        Optional<Application> optionalReservation = applicationRepository.findById(id);
        if (!optionalReservation.isPresent())
            throw new IllegalArgumentException("Wrong reservation selected");
        Application application = optionalReservation.get();
        if (!application.getUsername().equals(username))
            throw new IllegalArgumentException("Wrong reservation selected");

        returnReservationSeats(application);
        applicationRepository.deleteByIdAndUsername(id, username);
    }

    private void returnReservationSeats(Application application) {

    }

//    @Transactional
//    public void deleteCinema(Long id) {
//        applicationRepository.deleteAllByCinemaName(universityRepository.findById(id).get().getName());
//        universityRepository.deleteById(id);
//    }
//
//    @Transactional
//    public void deleteFilm(Long id, String cinemaName) {
//        Course course = courseRepository.findById(id).get();
//        if (!course.getUniversity().getName().equals(cinemaName))
//            throw new IllegalArgumentException(String.format("Cinema %s has no film with id: %s", cinemaName, id.toString()));
//        applicationRepository.deleteAllByFilmName(course.getName());
//        courseRepository.deleteById(id);
//    }
}