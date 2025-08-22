package com.gym.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one-to-one to users (unique FK)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_trainer_user"))
    private User user;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_trainer_specialization"))
    private TrainingType specialization;

    // inverse side of many-to-many (optional mapping)
    @ManyToMany(mappedBy = "trainers")
    private Set<Trainee> trainees = new HashSet<>();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public TrainingType getSpecialization() { return specialization; }
    public void setSpecialization(TrainingType specialization) { this.specialization = specialization; }
    public Set<Trainee> getTrainees() { return trainees; }
    public void setTrainees(Set<Trainee> trainees) { this.trainees = trainees; }

    @Override public boolean equals(Object o) {
        if (this == o) return true; if (!(o instanceof Trainer)) return false;
        return Objects.equals(id, ((Trainer) o).id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
