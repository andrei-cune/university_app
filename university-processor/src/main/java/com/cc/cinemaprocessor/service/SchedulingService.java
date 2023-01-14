package com.cc.cinemaprocessor.service;

import com.cc.cinemaprocessor.repository.CourseRepository;
import com.cc.cinemaprocessor.repository.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
//        filmRepository.deleteAllByStartTimeLessThan(yesterday);
//        reservationRepository.deleteAllByStartTimeLessThan(yesterday);
//        log.info("Deleted films and reservations for previous date");
//    }
}