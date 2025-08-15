package com.gym.gymapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class Trainee {
    private Long id;
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;

    public Trainee() {}

    public Trainee(Long userId, LocalDate dateOfBirth, String address) {
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    // Getters and setters. lombok
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainee trainee = (Trainee) o;
        return Objects.equals(id, trainee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
