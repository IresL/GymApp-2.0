package com.gym.gymapp.service;

import com.gym.gymapp.exception.NotFoundException;
import com.gym.gymapp.model.*;
import com.gym.gymapp.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;

    public TrainerService(TrainerRepository trainerRepository,
                          TrainingRepository trainingRepository,
                          TrainingTypeRepository trainingTypeRepository,
                          UserRepository userRepository,
                          UserService userService,
                          AuthService authService) {
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.authService = authService;
    }

    public UserService.CreatedUser create(String firstName, String lastName, boolean active, String specialization) {
        TrainingType type = trainingTypeRepository.findByNameIgnoreCase(specialization)
                .orElseThrow(() -> new NotFoundException("TrainingType not found: " + specialization));

        var created = userService.createUser(firstName, lastName, active);
        User u = created.user();

        Trainer tr = new Trainer();
        tr.setUser(u);
        tr.setSpecialization(type);
        trainerRepository.save(tr);

        log.info("Created trainer id={} (user={}, spec={})", tr.getId(), u.getUsername(), type.getName());
        return created;
    }

    @Transactional(readOnly = true)
    public Optional<Trainer> getByUsername(String username) {
        return trainerRepository.findByUser_UsernameIgnoreCase(username);
    }

    public Trainer update(String authUser, String authPass, String username, String specializationOrNull) {
        authService.requireAuth(authUser, authPass);

        Trainer tr = trainerRepository.findByUser_UsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));

        if (specializationOrNull != null) {
            TrainingType type = trainingTypeRepository.findByNameIgnoreCase(specializationOrNull)
                    .orElseThrow(() -> new NotFoundException("TrainingType not found: " + specializationOrNull));
            tr.setSpecialization(type);
        }

        trainerRepository.save(tr);
        log.info("Updated trainer username={}", username);
        return tr;
    }

    public void setActive(String authUser, String authPass, String username, boolean active) {
        authService.requireAuth(authUser, authPass);
        if (active) userService.activate(username); else userService.deactivate(username);
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainings(String authUser, String authPass,
                                       String trainerUsername,
                                       LocalDate from, LocalDate to, String traineeNameLike) {
        authService.requireAuth(authUser, authPass);
        return trainingRepository.findByTrainerUsernameWithFilters(trainerUsername, from, to, traineeNameLike);
    }
}
