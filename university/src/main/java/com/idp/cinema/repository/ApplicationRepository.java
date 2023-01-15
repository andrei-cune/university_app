package com.idp.cinema.repository;

import com.idp.cinema.model.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
    List<Application> findAllByUsername(String Username);
    void deleteByIdAndUsername(Long id, String username);
    void deleteAllByUniversityName(String universityName);
}
