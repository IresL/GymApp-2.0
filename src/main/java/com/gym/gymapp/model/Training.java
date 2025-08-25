package com.gym.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="trainings", indexes = {
        @Index(name="idx_trainings_trainee", columnList="trainee_id"),
        @Index(name="idx_trainings_trainer", columnList="trainer_id")
})
public class Training {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="trainee_id", nullable=false,
            foreignKey=@ForeignKey(name="fk_training_trainee"))
    private Trainee trainee;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="trainer_id", nullable=false,
            foreignKey=@ForeignKey(name="fk_training_trainer"))
    private Trainer trainer;

    @NotBlank @Column(name="training_name", nullable=false, length=200)
    private String trainingName;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="training_type_id", nullable=false,
            foreignKey=@ForeignKey(name="fk_training_type"))
    private TrainingType trainingType;

    @NotNull @Column(name="training_date", nullable=false)
    private LocalDate trainingDate;

    @NotNull @Positive @Column(name="training_duration", nullable=false)
    private Integer trainingDuration;

    public Long getId(){ return id; } public void setId(Long id){ this.id=id; }
    public Trainee getTrainee(){ return trainee; } public void setTrainee(Trainee t){ this.trainee=t; }
    public Trainer getTrainer(){ return trainer; } public void setTrainer(Trainer t){ this.trainer=t; }
    public String getTrainingName(){ return trainingName; } public void setTrainingName(String n){ this.trainingName=n; }
    public TrainingType getTrainingType(){ return trainingType; } public void setTrainingType(TrainingType tt){ this.trainingType=tt; }
    public LocalDate getTrainingDate(){ return trainingDate; } public void setTrainingDate(LocalDate d){ this.trainingDate=d; }
    public Integer getTrainingDuration(){ return trainingDuration; } public void setTrainingDuration(Integer m){ this.trainingDuration=m; }

    // Optional helpers (დამხმარე, რომ ძველი კოდი გაგიაროს)
    @Transient public Long getTraineeId(){ return trainee!=null ? trainee.getId():null; }
    @Transient public Long getTrainerId(){ return trainer!=null ? trainer.getId():null; }

    @Override public boolean equals(Object o){ return this==o || (o instanceof Training t && Objects.equals(id,t.id)); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
