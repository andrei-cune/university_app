package com.cc.cinemaprocessor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table( name = "university",uniqueConstraints={@UniqueConstraint(columnNames = {"name"})})
public class University {

    @Id
    @Column(name = "university_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("university_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "university",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Course> courses = new ArrayList<>();
}
