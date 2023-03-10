package com.idp.cinema.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateFilmRequest {
    @JsonProperty("cinema_name")
    private String cinemaName;
    @JsonProperty("film")
    private Course course;
}
