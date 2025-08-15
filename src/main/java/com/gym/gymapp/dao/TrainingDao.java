package com.gym.gymapp.dao;

import com.gym.gymapp.model.Training;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TrainingDao {

    private final Map<Long, Training> storage;
    private long seq = 1L;

    public TrainingDao(@Qualifier("trainingStorage") Map<Long, Training> storage) {
        this.storage = storage;
    }

    public Training save(Training training) {
        if (training.getId() == null) {
            training.setId(seq++);
        }
        storage.put(training.getId(), training);
        return training;
    }

    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Training> findAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Training> findByTraineeId(Long traineeId) {
        return storage.values().stream()
                .filter(t -> traineeId.equals(t.getTraineeId()))
                .collect(Collectors.toList());
    }

    public List<Training> findByTrainerId(Long trainerId) {
        return storage.values().stream()
                .filter(t -> trainerId.equals(t.getTrainerId()))
                .collect(Collectors.toList());
    }
}
