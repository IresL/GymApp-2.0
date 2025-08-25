package com.gym.gymapp.service;

import com.gym.gymapp.exception.NotFoundException;
import com.gym.gymapp.exception.ValidationException;
import com.gym.gymapp.model.*;
import com.gym.gymapp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingService(TrainingRepository trainingRepository,
                           TraineeRepository traineeRepository,
                           TrainerRepository trainerRepository,
                           TrainingTypeRepository trainingTypeRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public Training add(String traineeUsername,
                        String trainerUsername,
                        String trainingName,
                        String trainingTypeName,
                        LocalDate trainingDate,
                        Integer durationMinutes) {

        if (trainingName == null || trainingName.isBlank())
            throw new ValidationException("trainingName required");
        if (trainingDate == null)
            throw new ValidationException("trainingDate required");
        if (durationMinutes == null || durationMinutes <= 0)
            throw new ValidationException("trainingDuration must be > 0");

        Trainee trainee = traineeRepository.findByUser_UsernameIgnoreCase(traineeUsername)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + traineeUsername));
        Trainer trainer = trainerRepository.findByUser_UsernameIgnoreCase(trainerUsername)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + trainerUsername));
        TrainingType type = trainingTypeRepository.findByNameIgnoreCase(trainingTypeName)
                .orElseThrow(() -> new NotFoundException("TrainingType not found: " + trainingTypeName));

        Training t = new Training();
        t.setTrainee(trainee);
        t.setTrainer(trainer);
        t.setTrainingName(trainingName);
        t.setTrainingType(type);
        t.setTrainingDate(trainingDate);
        t.setTrainingDuration(durationMinutes);

        Training saved = trainingRepository.save(t);
        log.info("Created training id={} [{}] trainee={} trainer={}",
                saved.getId(), trainingName, traineeUsername, trainerUsername);
        return saved;
    }
}
