package com.gym.gymapp.repository;

import com.gym.gymapp.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    // By trainee username + optional filters
    @Query("""
        select trn from Training trn
          join trn.trainer tr
          join tr.user tru
        where lower(trn.trainee.user.username) = lower(?1)
          and (?2 is null or trn.trainingDate >= ?2)
          and (?3 is null or trn.trainingDate <= ?3)
          and (?4 is null or lower(concat(tru.firstName,' ',tru.lastName)) like lower(concat('%', ?4, '%')))
          and (?5 is null or lower(trn.trainingType.name) = lower(?5))
        order by trn.trainingDate desc, trn.id desc
        """)
    List<Training> findByTraineeUsernameWithFilters(String traineeUsername,
                                                    LocalDate from,
                                                    LocalDate to,
                                                    String trainerFullNameLike,
                                                    String trainingTypeName);

    // By trainer username + optional filters
    @Query("""
        select trn from Training trn
          join trn.trainee tn
          join tn.user tnu
        where lower(trn.trainer.user.username) = lower(?1)
          and (?2 is null or trn.trainingDate >= ?2)
          and (?3 is null or trn.trainingDate <= ?3)
          and (?4 is null or lower(concat(tnu.firstName,' ',tnu.lastName)) like lower(concat('%', ?4, '%')))
        order by trn.trainingDate desc, trn.id desc
        """)
    List<Training> findByTrainerUsernameWithFilters(String trainerUsername,
                                                    LocalDate from,
                                                    LocalDate to,
                                                    String traineeFullNameLike);
}
