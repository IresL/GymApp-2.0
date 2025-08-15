package com.gym.gymapp.dao;

import com.gym.gymapp.model.Training;
import com.gym.gymapp.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TrainingDao {
    private static final Logger logger = LoggerFactory.getLogger(TrainingDao.class);
    private static final String NAMESPACE = "trainings";

    private InMemoryStorage storage;

    @Autowired
    public void setStorage(InMemoryStorage storage) {
        this.storage = storage;
    }

    public Training save(Training training) {
        if (training.getId() == null) {
            training.setId(storage.generateId(NAMESPACE));
        }
        storage.save(NAMESPACE, training.getId(), training);
        logger.info("Saved training with id: {}", training.getId());
        return training;
    }

    public Optional<Training> findById(Long id) {
        Training training = storage.findById(NAMESPACE, id);
        logger.debug("Finding training by id {}: {}", id, training != null ? "found" : "not found");
        return Optional.ofNullable(training);
    }

    public List<Training> findByTraineeId(Long traineeId) {
        Collection<Training> trainings = storage.<Training>getNamespace(NAMESPACE).values();
        List<Training> result = trainings.stream()
                .filter(t -> traineeId.equals(t.getTraineeId()))
                .collect(Collectors.toList());
        logger.debug("Finding trainings by traineeId {}: {} found", traineeId, result.size());
        return result;
    }

    public List<Training> findByTrainerId(Long trainerId) {
        Collection<Training> trainings = storage.<Training>getNamespace(NAMESPACE).values();
        List<Training> result = trainings.stream()
                .filter(t -> trainerId.equals(t.getTrainerId()))
                .collect(Collectors.toList());
        logger.debug("Finding trainings by trainerId {}: {} found", trainerId, result.size());
        return result;
    }

    public Collection<Training> findAll() {
        Collection<Training> trainings = storage.<Training>getNamespace(NAMESPACE).values();
        logger.debug("Finding all trainings: {} found", trainings.size());
        return trainings;
    }
}

