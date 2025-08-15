package com.gym.gymapp.dao;

import com.gym.gymapp.model.Trainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TrainerDao {

    private final Map<Long, Trainer> storage;
    private long seq = 1L;

    public TrainerDao(@Qualifier("trainerStorage") Map<Long, Trainer> storage) {
        this.storage = storage;
    }

    public Trainer save(Trainer trainer) {
        if (trainer.getId() == null) {
            trainer.setId(seq++);
        }
        storage.put(trainer.getId(), trainer);
        return trainer;
    }

    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Trainer> findAll() {
        return new ArrayList<>(storage.values());
    }
}
