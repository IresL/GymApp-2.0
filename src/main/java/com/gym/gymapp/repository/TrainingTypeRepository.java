package com.gym.gymapp.repository;

import com.gym.gymapp.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> findByNameIgnoreCase(String name);
}
