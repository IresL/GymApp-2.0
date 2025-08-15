package com.gym.gymapp.service;

import com.gym.gymapp.dao.TraineeDao;
import com.gym.gymapp.dao.UserDao;
import com.gym.gymapp.model.Trainee;
import com.gym.gymapp.model.User;
import com.gym.gymapp.util.PasswordGenerator;
import com.gym.gymapp.util.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private TraineeDao traineeDao;
    private UserDao userDao;
    private UsernameGenerator usernameGenerator;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    public User createTrainee(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        logger.info("Creating new trainee: {} {}", firstName, lastName);

        // Create User
        User user = new User(firstName, lastName);
        user.setUsername(usernameGenerator.generateUsername(firstName, lastName));
        user.setPassword(PasswordGenerator.generatePassword());
        user = userDao.save(user);

        // Create Trainee
        Trainee trainee = new Trainee(user.getId(), dateOfBirth, address);
        traineeDao.save(trainee);

        logger.info("Successfully created trainee with username: {}", user.getUsername());
        return user;
    }

    public Optional<Trainee> selectTrainee(Long traineeId) {
        logger.debug("Selecting trainee with id: {}", traineeId);
        return traineeDao.findById(traineeId);
    }

    public Optional<Trainee> selectTraineeByUserId(Long userId) {
        logger.debug("Selecting trainee by user id: {}", userId);
        return traineeDao.findByUserId(userId);
    }

    public Optional<Trainee> updateTrainee(Long traineeId, String firstName, String lastName,
                                           LocalDate dateOfBirth, String address, Boolean isActive) {
        logger.info("Updating trainee with id: {}", traineeId);

        Optional<Trainee> traineeOpt = traineeDao.findById(traineeId);
        if (traineeOpt.isPresent()) {
            Trainee trainee = traineeOpt.get();

            // Update trainee fields
            trainee.setDateOfBirth(dateOfBirth);
            trainee.setAddress(address);
            traineeDao.save(trainee);

            // Update user fields
            Optional<User> userOpt = userDao.findById(trainee.getUserId());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setIsActive(isActive);
                userDao.save(user);
            }

            logger.info("Successfully updated trainee with id: {}", traineeId);
            return Optional.of(trainee);
        }

        logger.warn("Trainee with id {} not found for update", traineeId);
        return Optional.empty();
    }

    public boolean deleteTrainee(Long traineeId) {
        logger.info("Deleting trainee with id: {}", traineeId);

        Optional<Trainee> traineeOpt = traineeDao.findById(traineeId);
        if (traineeOpt.isPresent()) {
            Trainee trainee = traineeOpt.get();

            // Delete trainee
            traineeDao.delete(traineeId);

            // Delete associated user
            userDao.delete(trainee.getUserId());

            logger.info("Successfully deleted trainee with id: {}", traineeId);
            return true;
        }

        logger.warn("Trainee with id {} not found for deletion", traineeId);
        return false;
    }
}
