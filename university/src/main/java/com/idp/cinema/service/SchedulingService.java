package com.idp.cinema.service;

import com.idp.cinema.repository.CourseRepository;
import com.idp.cinema.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
public class SchedulingService {

    private final CourseRepository courseRepository;
    private final ApplicationRepository applicationRepository;

    public SchedulingService(CourseRepository courseRepository, ApplicationRepository applicationRepository) {
        this.courseRepository = courseRepository;
        this.applicationRepository = applicationRepository;
    }

//    @Scheduled(cron = "0 0 1 * * ?")
//    @Transactional
//    public void deleteFilmsAndReservations() {
//        LocalDateTime yesterday = LocalDateTime.now().minusDays(1L);
//        courseRepository.deleteAllByStartTimeLessThan(yesterday);
//        applicationRepository.deleteAllByStartTimeLessThan(yesterday);
//        log.info("Deleted films and reservations for previous date");
//    }
}
