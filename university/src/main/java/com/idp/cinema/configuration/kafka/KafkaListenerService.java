package com.idp.cinema.configuration.kafka;


import com.idp.cinema.model.Application;
import com.idp.cinema.service.UniversityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

@EnableKafka
@Configuration
@Slf4j
public class KafkaListenerService {

    @Autowired
    UniversityService universityService;

//    private KafkaListenerService() {
//    }
//    private KafkaListenerService(UniversityService universityService) {
//        this.universityService = universityService;
//    }

    @KafkaListener(topics = "students", containerFactory = "kafkaListenerContainerFactory")
    public void applicationListener(Application application, Acknowledgment ack) {
        log.info(String.format("Received student application from topic \"students\": [%s]", application));
        universityService.saveApplication( application);
        ack.acknowledge();
    }
}

