package com.idp.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "course")
@Getter
@Setter
@ToString
public class Course {

    @Id
    @Column(name = "course_id")
    @JsonProperty("course_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
