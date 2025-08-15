package com.gym.gymapp.dao;

import com.gym.gymapp.model.Trainer;
import com.gym.gymapp.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

@Repository
public class TrainerDao {
    private static final Logger logger = LoggerFactory.getLogger(TrainerDao.class);
    private static final String NAMESPACE = "trainers";

    private InMemoryStorage storage;

    @Autowired
    public void setStorage(InMemoryStorage storage) {
        this.storage = storage;
    }

    public Trainer save(Trainer trainer) {
        if (trainer.getId() == null) {
            trainer.setId(storage.generateId(NAMESPACE));
        }
        storage.save(NAMESPACE, trainer.getId(), trainer);
        logger.info("Saved trainer with id: {}", trainer.getId());
        return trainer;
    }

    public Optional<Trainer> findById(Long id) {
        Trainer trainer = storage.findById(NAMESPACE, id);
        logger.debug("Finding trainer by id {}: {}", id, trainer != null ? "found" : "not found");
        return Optional.ofNullable(trainer);
    }

    public Optional<Trainer> findByUserId(Long userId) {
        Collection<Trainer> trainers = storage.<Trainer>getNamespace(NAMESPACE).values();
        Optional<Trainer> trainer = trainers.stream()
                .filter(t -> userId.equals(t.getUserId()))
                .findFirst();
        logger.debug("Finding trainer by userId {}: {}", userId, trainer.isPresent() ? "found" : "not found");
        return trainer;
    }

    public Collection<Trainer> findAll() {
        Collection<Trainer> trainers = storage.<Trainer>getNamespace(NAMESPACE).values();
        logger.debug("Finding all trainers: {} found", trainers.size());
        return trainers;
    }
}


