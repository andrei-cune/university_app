package com.idp.cinema.repository;

import com.idp.cinema.model.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<University, Long> {
    University findByNameEquals(String universityName);
}
