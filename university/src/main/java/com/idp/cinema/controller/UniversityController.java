package com.idp.cinema.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idp.cinema.configuration.CinemaProperties;
import com.idp.cinema.configuration.JwtTokenUtil;
import com.idp.cinema.model.University;
import com.idp.cinema.model.Course;
import com.idp.cinema.model.Application;
import com.idp.cinema.service.UniversityService;
import com.idp.cinema.service.RabbitSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class UniversityController {
    private final UniversityService universityService;
    private final JwtTokenUtil jwtTokenUtil;
    private final CinemaProperties cinemaProperties;
    private final ObjectMapper objectMapper;
//    private final RabbitSender rabbitSender;


    @Autowired
    KafkaTemplate<String, Application> kafkaTemplate;

    public UniversityController(UniversityService universityService, JwtTokenUtil jwtTokenUtil, CinemaProperties cinemaProperties, ObjectMapper objectMapper) {
        this.universityService = universityService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.cinemaProperties = cinemaProperties;
        this.objectMapper = objectMapper;
//        this.rabbitSender = rabbitSender;
    }


    @GetMapping(value = "/universities")
    public ResponseEntity<List<University>> getCinemas() {
        List<University> universities = universityService.getUniversities();
        return new ResponseEntity<>(universities, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER_ADMIN')")
    @PostMapping(value = "/universities")
    public ResponseEntity<Long> addUniversity(@RequestBody University university) {
        if (university.getName() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            log.info("Sending create university request: " + university.toString());
//            rabbitSender.sendMessage(cinemaProperties.getExchange(), cinemaProperties.getAdminRoutingKey(), objectMapper.writeValueAsString(university));
            universityService.saveUniversity(university);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAuthority('USER_ADMIN')")
    @DeleteMapping(value = "/universities/{id}")
    public void deleteCinema(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        log.info("Sending delete cinema request to queue with id: " + id);
//        rabbitSender.sendMessage(cinemaProperties.getExchange(), cinemaProperties.getAdminRoutingKey(), "DELETE_CINEMA " + id);

    }


    @GetMapping(value = "/universities/{universityName}/courses")
    public ResponseEntity<List<Course>> getCourses(@PathVariable String universityName) {
        List<Course> courses = universityService.getCourses(universityName);
        if (courses == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER_ADMIN')")
    @PostMapping(value = "/universities/{universityName}/courses")
    public ResponseEntity<Long> addFilm(@RequestBody Course course, @PathVariable String universityName) {
        if (course.getName() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        try {
//            log.info(String.format("Sending create film request to queue for cinema %s: %s", universityName, course.toString()));
//            rabbitSender.sendMessage(cinemaProperties.getExchange(), cinemaProperties.getAdminRoutingKey(), objectMapper.writeValueAsString(new CreateFilmRequest(universityName, course)));
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
        universityService.saveCourse(course, universityName);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PreAuthorize("hasAuthority('USER_ADMIN')")
    @DeleteMapping(value = "/universities/{universityName}/courses/{id}")
    public void deleteCourse(@PathVariable String universityName, @PathVariable Long id) {
//        log.info(String.format("Sending delete film request to queue for cinema %s: %s", universityName, id));
//        rabbitSender.sendMessage(cinemaProperties.getExchange(), cinemaProperties.getAdminRoutingKey(), String.format("DELETE_FILM %s %s", id, universityName));
        universityService.deleteCourse(id, universityName);
    }

    @GetMapping(value = "/applications")
    public ResponseEntity<List<Application>> getApplications(@RequestHeader("Authorization") String jwtToken) {
        String username = getUsernameFromToken(jwtToken);
        List<Application> applications = universityService.getApplications(username);
        if (applications == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @PostMapping(value = "/applications")
    public ResponseEntity<Long> submitApplication(
            @RequestHeader("Authorization") String jwtToken,
            @RequestBody Application application) throws JsonProcessingException {

        if (application.getUniversityName() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String username = getUsernameFromToken(jwtToken);
        application.setUsername(username);
        log.info(String.format("Sending application to Kafka topic: %s", application));
//        rabbitSender.sendMessage(cinemaProperties.getExchange(), cinemaProperties.getRoutingKey(), objectMapper.writeValueAsString(application));
        kafkaTemplate.send("students", application);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @DeleteMapping("/applications/{id}")
    public void deleteApplication(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable Long id) {

        String username = getUsernameFromToken(jwtToken);
        log.info(String.format("Sending delete reservation request to queue: %s", id));
//        rabbitSender.sendMessage(cinemaProperties.getExchange(), cinemaProperties.getRoutingKey(), String.format("DELETE_RESERVATION %s %s", id, username));
        universityService.deleteApplication(id, username);
    }

    private String getUsernameFromToken(String jwtToken) {
        return jwtTokenUtil.getUsername(jwtToken.split(" ")[1].trim());
    }
}
