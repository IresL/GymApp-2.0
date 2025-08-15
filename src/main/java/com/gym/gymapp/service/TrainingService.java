package com.gym.gymapp.service;

import com.gym.gymapp.dao.TrainingDao;
import com.gym.gymapp.model.Training;
import com.gym.gymapp.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training createTraining(Long traineeId, Long trainerId, String trainingName,
                                   TrainingType trainingType, LocalDate trainingDate, Integer trainingDuration) {
        logger.info("Creating new training: {} for trainee {} and trainer {}", trainingName, traineeId, trainerId);

        Training training = new Training(traineeId, trainerId, trainingName, trainingType, trainingDate, trainingDuration);
        training = trainingDao.save(training);

        logger.info("Successfully created training with id: {}", training.getId());
        return training;
    }

    public Optional<Training> selectTraining(Long trainingId) {
        logger.debug("Selecting training with id: {}", trainingId);
        return trainingDao.findById(trainingId);
    }

    public List<Training> selectTrainingsByTrainee(Long traineeId) {
        logger.debug("Selecting trainings for trainee id: {}", traineeId);
        return trainingDao.findByTraineeId(traineeId);
    }

    public List<Training> selectTrainingsByTrainer(Long trainerId) {
        logger.debug("Selecting trainings for trainer id: {}", trainerId);
        return trainingDao.findByTrainerId(trainerId);
    }
}
