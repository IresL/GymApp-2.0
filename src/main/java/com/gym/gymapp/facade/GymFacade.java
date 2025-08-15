package com.gym.gymapp.facade;

import com.gym.gymapp.model.Training;
import com.gym.gymapp.model.TrainingType;
import com.gym.gymapp.model.User;
import com.gym.gymapp.service.TraineeService;
import com.gym.gymapp.service.TrainerService;
import com.gym.gymapp.service.TrainingService;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Component
public class GymFacade {
    private static final Logger logger = LoggerFactory.getLogger(GymFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        logger.info("GymCrmFacade initialized with all services");
    }

    // Trainee operations
    public User createTrainee(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        logger.info("Facade: Creating trainee {} {}", firstName, lastName);
        return traineeService.createTrainee(firstName, lastName, dateOfBirth, address);
    }

    // Trainer operations
    public User createTrainer(String firstName, String lastName, TrainingType specialization) {
        logger.info("Facade: Creating trainer {} {} with specialization {}", firstName, lastName, specialization);
        return trainerService.createTrainer(firstName, lastName, specialization);
    }

    // Training operations
    public Training createTraining(Long traineeId, Long trainerId, String trainingName,
                                   TrainingType trainingType, LocalDate trainingDate, Integer trainingDuration) {
        logger.info("Facade: Creating training {} for trainee {} and trainer {}", trainingName, traineeId, trainerId);
        return trainingService.createTraining(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
    }
}
