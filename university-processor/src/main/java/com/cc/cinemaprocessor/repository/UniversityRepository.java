package com.cc.cinemaprocessor.repository;

import com.cc.cinemaprocessor.model.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<University, Long> {
    University findByNameEquals(String cinemaName);
}

