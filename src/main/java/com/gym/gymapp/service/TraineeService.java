package com.gym.gymapp.service;

import com.gym.gymapp.exception.NotFoundException;
import com.gym.gymapp.exception.ValidationException;
import com.gym.gymapp.model.*;
import com.gym.gymapp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;

    public TraineeService(TraineeRepository traineeRepository,
                          TrainerRepository trainerRepository,
                          TrainingRepository trainingRepository,
                          UserRepository userRepository,
                          UserService userService,
                          AuthService authService) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.authService = authService;
    }

    public UserService.CreatedUser create(String firstName, String lastName, boolean active,
                                          LocalDate dob, String address) {
        var created = userService.createUser(firstName, lastName, active);
        User u = created.user();

        Trainee t = new Trainee();
        t.setUser(u);
        t.setDateOfBirth(dob);
        t.setAddress(address);
        traineeRepository.save(t);

        log.info("Created trainee id={} (user={})", t.getId(), u.getUsername());
        return created; // username + raw password (ഒne-time)
    }

    @Transactional(readOnly = true)
    public Optional<Trainee> getByUsername(String username) {
        return traineeRepository.findByUser_UsernameIgnoreCase(username);
    }

    public Trainee update(String authUser, String authPass, String username,
                          LocalDate dob, String address) {
        authService.requireAuth(authUser, authPass);

        Trainee t = traineeRepository.findByUser_UsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + username));
        if (dob != null) t.setDateOfBirth(dob);
        if (address != null) t.setAddress(address);

        traineeRepository.save(t);
        log.info("Updated trainee username={}", username);
        return t;
    }

    public void setActive(String authUser, String authPass, String username, boolean active) {
        authService.requireAuth(authUser, authPass);
        if (active) userService.activate(username); else userService.deactivate(username);
    }

    public void deleteByUsername(String authUser, String authPass, String traineeUsername) {
        authService.requireAuth(authUser, authPass);

        // მოიტანე და წაშალე Trainee, მერე User (ან DB FK cascade გააკეთებს)
        Trainee t = traineeRepository.findByUser_UsernameIgnoreCase(traineeUsername)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + traineeUsername));
        traineeRepository.delete(t);

        User u = userRepository.findByUsernameIgnoreCase(traineeUsername)
                .orElseThrow(() -> new NotFoundException("User not found: " + traineeUsername));
        userRepository.delete(u);

        log.info("Deleted trainee and user username={}", traineeUsername);
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainings(String authUser, String authPass,
                                       String traineeUsername,
                                       LocalDate from, LocalDate to,
                                       String trainerNameLike, String trainingTypeName) {
        authService.requireAuth(authUser, authPass);
        return trainingRepository.findByTraineeUsernameWithFilters(
                traineeUsername, from, to, trainerNameLike, trainingTypeName);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getNotAssignedTrainers(String authUser, String authPass, String traineeUsername) {
        authService.requireAuth(authUser, authPass);
        return traineeRepository.findActiveTrainersNotAssignedToTrainee(traineeUsername);
    }

    public void updateTrainers(String authUser, String authPass,
                               String traineeUsername, List<String> trainerUsernames) {
        authService.requireAuth(authUser, authPass);
        if (trainerUsernames == null) trainerUsernames = List.of();

        Trainee trainee = traineeRepository.findByUser_UsernameIgnoreCase(traineeUsername)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + traineeUsername));

        Set<Trainer> target = trainerUsernames.stream()
                .map(u -> trainerRepository.findByUser_UsernameIgnoreCase(u)
                        .orElseThrow(() -> new NotFoundException("Trainer not found: " + u)))
                .collect(Collectors.toSet());

        trainee.setTrainers(target);
        traineeRepository.save(trainee);
        log.info("Updated trainee's trainers username={}, count={}", traineeUsername, target.size());
    }
}
