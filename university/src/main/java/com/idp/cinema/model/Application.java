package com.idp.cinema.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "application")
public class Application {

    @Id
    @Column(name = "application_id")
    @JsonProperty("application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("university_name")
    @Column(name = "university_name")
    private String universityName;

    @JsonProperty("first_name")
    @Column(name = "first_name")
    private String firstName;

    @JsonProperty("last_name")
    @Column(name = "last_name")
    private String lastName;

    @JsonProperty("date_of_birth")
    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @JsonProperty("pid")
    @Column(name = "pid")
    private String pid;

    @JsonProperty("address")
    @Column(name = "address")
    private String address;

    @JsonProperty("phone_number")
    @Column(name = "phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    @Column(name = "email")
    private String email;

    @JsonProperty("gpa")
    @Column(name = "gpa")
    private String gpa;

    @JsonProperty("username")
    @Column(name = "username")
    private String username;
}
