package com.gym.gymapp.service;

import com.gym.gymapp.dao.TraineeDao;
import com.gym.gymapp.dao.TrainerDao;
import com.gym.gymapp.dao.TrainingDao;
import com.gym.gymapp.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

    private TrainingDao trainingDao;
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) { this.trainingDao = trainingDao; }

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) { this.traineeDao = traineeDao; }

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) { this.trainerDao = trainerDao; }

    public Training create(Training training) {
        Objects.requireNonNull(training, "training is required");
        if (training.getTraineeId() == null) throw new IllegalArgumentException("training.traineeId is required");
        if (training.getTrainerId() == null) throw new IllegalArgumentException("training.trainerId is required");
        if (training.getTrainingName() == null) throw new IllegalArgumentException("training.trainingName is required");
        if (training.getTrainingType() == null) throw new IllegalArgumentException("training.trainingType is required");
        if (training.getTrainingDate() == null) throw new IllegalArgumentException("training.trainingDate is required");
        if (training.getTrainingDuration() == null) throw new IllegalArgumentException("training.trainingDuration is required");

        if (traineeDao.findById(training.getTraineeId()).isEmpty())
            throw new IllegalArgumentException("trainee not found: " + training.getTraineeId());
        if (trainerDao.findById(training.getTrainerId()).isEmpty())
            throw new IllegalArgumentException("trainer not found: " + training.getTrainerId());

        trainingDao.save(training);
        log.info("Created training id={} (traineeId={}, trainerId={})",
                training.getId(), training.getTraineeId(), training.getTrainerId());
        return training;
    }

    public Optional<Training> get(Long id) { return trainingDao.findById(id); }

    public List<Training> list() { return trainingDao.findAll(); }

    public List<Training> listByTrainee(Long traineeId) { return trainingDao.findByTraineeId(traineeId); }

    public List<Training> listByTrainer(Long trainerId) { return trainingDao.findByTrainerId(trainerId); }
}
