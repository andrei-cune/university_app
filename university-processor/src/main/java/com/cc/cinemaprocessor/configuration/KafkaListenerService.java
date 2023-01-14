package com.cc.cinemaprocessor.configuration;

import com.cc.cinemaprocessor.model.Application;
import com.cc.cinemaprocessor.service.UniversityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@EnableKafka
@Configuration
@Slf4j
public class KafkaListenerService {

    private final UniversityService universityService;

    private KafkaListenerService(UniversityService universityService) {
        this.universityService = universityService;
    }

   @KafkaListener(topics = "students", containerFactory = "kafkaListenerContainerFactory")
    public void applicationListener(Application application) {
       log.info(String.format("Received student application from topic \"students\": [%s]", application));
        universityService.saveApplication(application);
    }
}
