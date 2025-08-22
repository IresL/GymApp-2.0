package com.gym.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "trainings", indexes = {
        @Index(name = "idx_trainings_trainee", columnList = "trainee_id"),
        @Index(name = "idx_trainings_trainer", columnList = "trainer_id")
})
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK -> Trainee
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_training_trainee"))
    private Trainee trainee;

    // FK -> Trainer
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_training_trainer"))
    private Trainer trainer;

    @NotBlank
    @Column(name = "training_name", nullable = false, length = 200)
    private String trainingName;

    // FK -> TrainingType
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_training_type"))
    private TrainingType trainingType;

    @NotNull
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @NotNull @Positive
    @Column(name = "training_duration", nullable = false)
    private Integer trainingDuration;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Trainee getTrainee() { return trainee; }
    public void setTrainee(Trainee trainee) { this.trainee = trainee; }
    public Trainer getTrainer() { return trainer; }
    public void setTrainer(Trainer trainer) { this.trainer = trainer; }
    public String getTrainingName() { return trainingName; }
    public void setTrainingName(String trainingName) { this.trainingName = trainingName; }
    public TrainingType getTrainingType() { return trainingType; }
    public void setTrainingType(TrainingType trainingType) { this.trainingType = trainingType; }
    public LocalDate getTrainingDate() { return trainingDate; }
    public void setTrainingDate(LocalDate trainingDate) { this.trainingDate = trainingDate; }
    public Integer getTrainingDuration() { return trainingDuration; }
    public void setTrainingDuration(Integer trainingDuration) { this.trainingDuration = trainingDuration; }

    @Override public boolean equals(Object o) {
        if (this == o) return true; if (!(o instanceof Training)) return false;
        return Objects.equals(id, ((Training) o).id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
