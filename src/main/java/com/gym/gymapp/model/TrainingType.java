package com.gym.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Table(name="training_types", indexes={
        @Index(name="ux_training_types_name", columnList="name", unique=true)
})
public class TrainingType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(name="name", nullable=false, length=50, unique=true)
    private String name;

    public Long getId(){ return id; } public void setId(Long id){ this.id=id; }
    public String getName(){ return name; } public void setName(String name){ this.name=name; }

    @Override public boolean equals(Object o){ return this==o || (o instanceof TrainingType t && Objects.equals(id,t.id)); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
