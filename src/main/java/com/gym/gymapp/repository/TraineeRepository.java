package com.gym.gymapp.repository;

import com.gym.gymapp.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    Optional<Trainee> findByUser_UsernameIgnoreCase(String username);

    // Find active trainers NOT assigned to the given trainee (by trainee username)
    @Query("""
           select tr from Trainer tr
           where tr.user.isActive = true
             and tr.id not in (
                 select t.id from Trainee tn join tn.trainers t
                 where lower(tn.user.username) = lower(?1)
             )
           """)
    List<com.gym.gymapp.model.Trainer> findActiveTrainersNotAssignedToTrainee(String traineeUsername);
}
