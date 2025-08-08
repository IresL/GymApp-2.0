package com.gym.gymapp.model;



import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users") // "user" არის PostgreSQL-ში reserved word
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isActive = true;
}

