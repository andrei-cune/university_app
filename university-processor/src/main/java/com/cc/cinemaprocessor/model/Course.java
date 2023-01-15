package com.cc.cinemaprocessor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "course")
@Getter
@Setter
public class Course {

    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("course_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name= "scheduled_at")
    @JsonProperty("scheduled_at")
    private String scheduledAt;

    @Column(name= "professor")
    @JsonProperty("professor")
    private String professor;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private University university;

}
