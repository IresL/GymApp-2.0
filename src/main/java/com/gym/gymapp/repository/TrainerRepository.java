package com.gym.gymapp.repository;

import com.gym.gymapp.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUser_UsernameIgnoreCase(String username);
}

