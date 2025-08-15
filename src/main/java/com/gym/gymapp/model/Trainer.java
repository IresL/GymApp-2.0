package com.gym.gymapp.model;

import java.util.Objects;

public class Trainer {
    private Long id;
    private Long userId;
    private TrainingType specialization;

    public Trainer() {
    }

    public Trainer(Long userId, TrainingType specialization) {
        this.userId = userId;
        this.specialization = specialization;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public TrainingType getSpecialization() { return specialization; }
    public void setSpecialization(TrainingType specialization) { this.specialization = specialization; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trainer trainer)) return false;
        return Objects.equals(id, trainer.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
