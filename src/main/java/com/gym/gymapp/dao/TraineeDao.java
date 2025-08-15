package com.gym.gymapp.dao;

import com.gym.gymapp.model.Trainee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TraineeDao {

    private final Map<Long, Trainee> storage;
    private long seq = 1L;

    public TraineeDao(@Qualifier("traineeStorage") Map<Long, Trainee> storage) {
        this.storage = storage;
    }

    public Trainee save(Trainee trainee) {
        if (trainee.getId() == null) {
            trainee.setId(seq++);
        }
        storage.put(trainee.getId(), trainee);
        return trainee;
    }

    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Trainee> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }
}
