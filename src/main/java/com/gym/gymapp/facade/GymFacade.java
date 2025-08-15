package com.gym.gymapp.facade;

import com.gym.gymapp.model.Trainee;
import com.gym.gymapp.model.Trainer;
import com.gym.gymapp.model.Training;
import com.gym.gymapp.service.TraineeService;
import com.gym.gymapp.service.TrainerService;
import com.gym.gymapp.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {
    private static final Logger log = LoggerFactory.getLogger(GymFacade.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(TraineeService traineeService,
                     TrainerService trainerService,
                     TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    // Trainee
    public Trainee createTrainee(String firstName, String lastName, Boolean isActive, Trainee trainee) {
        return traineeService.create(firstName, lastName, isActive, trainee);
    }
    public Trainee updateTrainee(Trainee t) { return traineeService.update(t); }
    public boolean deleteTrainee(Long id) { return traineeService.delete(id); }
    public Optional<Trainee> getTrainee(Long id) { return traineeService.get(id); }
    public List<Trainee> listTrainees() { return traineeService.list(); }

    // Trainer
    public Trainer createTrainer(String firstName, String lastName, Boolean isActive, Trainer trainer) {
        return trainerService.create(firstName, lastName, isActive, trainer);
    }
    public Trainer updateTrainer(Trainer t) { return trainerService.update(t); }
    public Optional<Trainer> getTrainer(Long id) { return trainerService.get(id); } // <- აქაა გამოსწორება
    public List<Trainer> listTrainers() { return trainerService.list(); }

    // Training
    public Training createTraining(Training t) { return trainingService.create(t); }
    public Optional<Training> getTraining(Long id) { return trainingService.get(id); }
    public List<Training> listTrainings() { return trainingService.list(); }
    public List<Training> listTrainingsByTrainee(Long traineeId) { return trainingService.listByTrainee(traineeId); }
    public List<Training> listTrainingsByTrainer(Long trainerId) { return trainingService.listByTrainer(trainerId); }
}
