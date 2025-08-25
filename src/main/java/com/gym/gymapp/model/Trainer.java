package com.gym.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="trainers")
public class Trainer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false, unique=true,
            foreignKey=@ForeignKey(name="fk_trainer_user"))
    private User user;

    @NotNull
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="specialization_id", nullable=false,
            foreignKey=@ForeignKey(name="fk_trainer_specialization"))
    private TrainingType specialization;

    @ManyToMany(mappedBy="trainers")
    private Set<Trainee> trainees = new HashSet<>();

    public Long getId(){ return id; } public void setId(Long id){ this.id=id; }
    public User getUser(){ return user; } public void setUser(User user){ this.user=user; }
    public TrainingType getSpecialization(){ return specialization; } public void setSpecialization(TrainingType s){ this.specialization=s; }
    public Set<Trainee> getTrainees(){ return trainees; } public void setTrainees(Set<Trainee> t){ this.trainees=t; }

    @Override public boolean equals(Object o){ return this==o || (o instanceof Trainer t && Objects.equals(id,t.id)); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
