package com.cc.cinemaprocessor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFilmRequest {
    @JsonProperty("cinema_name")
    private String cinemaName;
    @JsonProperty("film")
    private Course course;
    public CreateFilmRequest(String cinemaName, Course course) {
        this.cinemaName = cinemaName;
        this.course = course;
    }

    public CreateFilmRequest(){}
}
