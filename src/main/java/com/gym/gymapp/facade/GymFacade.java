package com.gym.gymapp.facade;

import com.gym.gymapp.exception.NotFoundException;
import com.gym.gymapp.model.*;
import com.gym.gymapp.service.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class GymFacade {

    private final AuthService authService;
    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(AuthService authService,
                     UserService userService,
                     TraineeService traineeService,
                     TrainerService trainerService,
                     TrainingService trainingService) {
        this.authService = authService;
        this.userService = userService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    // 1 & 2
    public UserService.CreatedUser createTrainerProfile(String first, String last, boolean active, String specialization) {
        return trainerService.create(first, last, active, specialization);
    }
    public UserService.CreatedUser createTraineeProfile(String first, String last, boolean active, LocalDate dob, String addr) {
        return traineeService.create(first, last, active, dob, addr);
    }

    // 3 & 4
    public boolean traineeAuth(String username, String password) {
        try { authService.requireAuth(username, password);
            return traineeService.getByUsername(username).isPresent();
        } catch (Exception e){ return false; }
    }
    public boolean trainerAuth(String username, String password) {
        try { authService.requireAuth(username, password);
            return trainerService.getByUsername(username).isPresent();
        } catch (Exception e){ return false; }
    }

    // 5 & 6
    public Trainer getTrainerProfile(String authUser, String authPass, String username) {
        authService.requireAuth(authUser, authPass);
        return trainerService.getByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));
    }
    public Trainee getTraineeProfile(String authUser, String authPass, String username) {
        authService.requireAuth(authUser, authPass);
        return traineeService.getByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + username));
    }

    // 7 & 8
    public void changeTraineePassword(String authUser, String authPass, String username, String oldPass, String newPass) {
        authService.requireAuth(authUser, authPass); userService.changePassword(username, oldPass, newPass);
    }
    public void changeTrainerPassword(String authUser, String authPass, String username, String oldPass, String newPass) {
        authService.requireAuth(authUser, authPass); userService.changePassword(username, oldPass, newPass);
    }

    // 9 & 10
    public Trainer updateTrainerProfile(String authUser, String authPass, String username, String newSpecOrNull) {
        return trainerService.update(authUser, authPass, username, newSpecOrNull);
    }
    public Trainee updateTraineeProfile(String authUser, String authPass, String username, LocalDate dob, String addr) {
        return traineeService.update(authUser, authPass, username, dob, addr);
    }

    // 11 & 12
    public void setTraineeActive(String authUser, String authPass, String username, boolean active) {
        traineeService.setActive(authUser, authPass, username, active);
    }
    public void setTrainerActive(String authUser, String authPass, String username, boolean active) {
        trainerService.setActive(authUser, authPass, username, active);
    }

    // 13
    public void deleteTraineeByUsername(String authUser, String authPass, String username) {
        traineeService.deleteByUsername(authUser, authPass, username);
    }

    // 14 & 15
    public List<Training> getTraineeTrainings(String authUser, String authPass, String traineeUsername,
                                              LocalDate from, LocalDate to, String trainerNameLike, String trainingType) {
        return traineeService.getTrainings(authUser, authPass, traineeUsername, from, to, trainerNameLike, trainingType);
    }
    public List<Training> getTrainerTrainings(String authUser, String authPass, String trainerUsername,
                                              LocalDate from, LocalDate to, String traineeNameLike) {
        return trainerService.getTrainings(authUser, authPass, trainerUsername, from, to, traineeNameLike);
    }

    // 16
    public Training addTraining(String authUser, String authPass,
                                String traineeUsername, String trainerUsername,
                                String trainingName, String trainingType, LocalDate date, Integer duration) {
        authService.requireAuth(authUser, authPass);
        return trainingService.add(traineeUsername, trainerUsername, trainingName, trainingType, date, duration);
    }

    // 17 & 18
    public List<Trainer> listActiveNotAssignedTrainers(String authUser, String authPass, String traineeUsername) {
        return traineeService.getNotAssignedTrainers(authUser, authPass, traineeUsername);
    }
    public void updateTraineeTrainers(String authUser, String authPass, String traineeUsername, List<String> trainerUsernames) {
        traineeService.updateTrainers(authUser, authPass, traineeUsername, trainerUsernames);
    }
}
