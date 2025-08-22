package com.gym.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trainees")
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one-to-one to users (unique FK)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_trainee_user"))
    private User user;

    @Past
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", length = 255)
    private String address;

    // many-to-many with trainers via join table
    @ManyToMany
    @JoinTable(name = "trainee2trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id"))
    private Set<Trainer> trainers = new HashSet<>();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Set<Trainer> getTrainers() { return trainers; }
    public void setTrainers(Set<Trainer> trainers) { this.trainers = trainers; }

    @Override public boolean equals(Object o) {
        if (this == o) return true; if (!(o instanceof Trainee)) return false;
        return Objects.equals(id, ((Trainee) o).id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
