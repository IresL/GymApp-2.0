package com.gym.gymapp.service;

import com.gym.gymapp.dao.TrainerDao;
import com.gym.gymapp.dao.UserDao;
import com.gym.gymapp.model.Trainer;
import com.gym.gymapp.model.TrainingType;
import com.gym.gymapp.model.User;
import com.gym.gymapp.util.PasswordGenerator;
import com.gym.gymapp.util.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDao trainerDao;
    private UserDao userDao;
    private UsernameGenerator usernameGenerator;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    public User createTrainer(String firstName, String lastName, TrainingType specialization) {
        logger.info("Creating new trainer: {} {} with specialization: {}", firstName, lastName, specialization);

        // Create User
        User user = new User(firstName, lastName);
        user.setUsername(usernameGenerator.generateUsername(firstName, lastName));
        user.setPassword(PasswordGenerator.generatePassword());
        user = userDao.save(user);

        // Create Trainer
        Trainer trainer = new Trainer(user.getId(), specialization);
        trainerDao.save(trainer);

        logger.info("Successfully created trainer with username: {}", user.getUsername());
        return user;
    }

    public Optional<Trainer> selectTrainer(Long trainerId) {
        logger.debug("Selecting trainer with id: {}", trainerId);
        return trainerDao.findById(trainerId);
    }

    public Optional<Trainer> selectTrainerByUserId(Long userId) {
        logger.debug("Selecting trainer by user id: {}", userId);
        return trainerDao.findByUserId(userId);
    }

    public Optional<Trainer> updateTrainer(Long trainerId, String firstName, String lastName, Boolean isActive) {
        logger.info("Updating trainer with id: {}", trainerId);

        Optional<Trainer> trainerOpt = trainerDao.findById(trainerId);
        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();

            // Update user fields (specialization is read-only)
            Optional<User> userOpt = userDao.findById(trainer.getUserId());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setIsActive(isActive);
                userDao.save(user);
            }

            logger.info("Successfully updated trainer with id: {}", trainerId);
            return Optional.of(trainer);
        }

        logger.warn("Trainer with id {} not found for update", trainerId);
        return Optional.empty();
    }
}