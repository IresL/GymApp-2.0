package com.gym.gymapp.dao;

import com.gym.gymapp.model.Trainee;
import com.gym.gymapp.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

@Repository
public class TraineeDao {
    private static final Logger logger = LoggerFactory.getLogger(TraineeDao.class);
    private static final String NAMESPACE = "trainees";

    private InMemoryStorage storage;

    @Autowired
    public void setStorage(InMemoryStorage storage) {
        this.storage = storage;
    }

    public Trainee save(Trainee trainee) {
        if (trainee.getId() == null) {
            trainee.setId(storage.generateId(NAMESPACE));
        }
        storage.save(NAMESPACE, trainee.getId(), trainee);
        logger.info("Saved trainee with id: {}", trainee.getId());
        return trainee;
    }

    public Optional<Trainee> findById(Long id) {
        Trainee trainee = storage.findById(NAMESPACE, id);
        logger.debug("Finding trainee by id {}: {}", id, trainee != null ? "found" : "not found");
        return Optional.ofNullable(trainee);
    }

    public Optional<Trainee> findByUserId(Long userId) {
        Collection<Trainee> trainees = storage.<Trainee>getNamespace(NAMESPACE).values();
        Optional<Trainee> trainee = trainees.stream()
                .filter(t -> userId.equals(t.getUserId()))
                .findFirst();
        logger.debug("Finding trainee by userId {}: {}", userId, trainee.isPresent() ? "found" : "not found");
        return trainee;
    }

    public Collection<Trainee> findAll() {
        Collection<Trainee> trainees = storage.<Trainee>getNamespace(NAMESPACE).values();
        logger.debug("Finding all trainees: {} found", trainees.size());
        return trainees;
    }

    public boolean delete(Long id) {
        boolean deleted = storage.delete(NAMESPACE, id);
        logger.info("Deleted trainee with id {}: {}", id, deleted);
        return deleted;
    }
}


