package com.cc.cinemaprocessor.configuration;

import com.cc.cinemaprocessor.model.University;
import com.cc.cinemaprocessor.model.CreateFilmRequest;
import com.cc.cinemaprocessor.model.Application;
import com.cc.cinemaprocessor.service.UniversityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableRabbit
public class RabbitCinemaListener {
    private final UniversityService universityService;

    public RabbitCinemaListener(UniversityService universityService) {
        this.universityService = universityService;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @RabbitListener(queues = "cinema-queue")
    public void receiveMessage(String message) throws JsonProcessingException {
        log.info(String.format("Received message on client queue: %s", message));

        if (message.contains("reservation_id")) {
            universityService.saveApplication(objectMapper().readValue(message, Application.class));
        }
        if (message.contains("DELETE_RESERVATION")) {
            String[] strings = message.split(" ");
            universityService.deleteReservation(Long.parseLong(strings[1]), strings[2]);
        }

    }

    @RabbitListener(queues = "cinema-admin-queue")
    public void receiveAdminMessage(String message) throws JsonProcessingException {
        log.info(String.format("Received message on admin queue: %s", message));
        if (message.contains("cinema_id")) {
            universityService.saveCinema(objectMapper().readValue(message, University.class));
        }
        if (message.contains("film_id")) {
            CreateFilmRequest createFilmRequest = objectMapper().readValue(message, CreateFilmRequest.class);
            universityService.saveFilm(createFilmRequest.getCourse(), createFilmRequest.getCinemaName());
        }
//        if (message.contains("DELETE_CINEMA")) {
//            universityService.deleteCinema(Long.parseLong(message.split(" ")[1]));
//        }
//        if (message.contains("DELETE_FILM")) {
//            String[] strings = message.split(" ");
//            universityService.deleteFilm(Long.parseLong(strings[1]), strings[2]);
//        }

    }
}
