package com.gym.gymapp.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.gymapp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;

@Component
public class StorageInitializer implements SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(StorageInitializer.class);

    @Value("${storage.init.file:}")
    private String initFile;

    private Map<Long, User> userStorage;
    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Training> trainingStorage;

    @Autowired
    public void setUserStorage(@Qualifier("userStorage") Map<Long, User> userStorage) {
        this.userStorage = userStorage;
    }

    @Autowired
    public void setTraineeStorage(@Qualifier("traineeStorage") Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Autowired
    public void setTrainerStorage(@Qualifier("trainerStorage") Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Autowired
    public void setTrainingStorage(@Qualifier("trainingStorage") Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Override
    public void afterSingletonsInstantiated() {
        if (initFile == null || initFile.isBlank()) {
            log.info("storage.init.file is empty — initial data loading skipped.");
            return;
        }

        try {
            InitData data = readJson(initFile);
            if (data != null) {
                if (data.getUsers() != null) {
                    data.getUsers().forEach(u -> userStorage.put(u.getId(), u));
                }
                if (data.getTrainees() != null) {
                    data.getTrainees().forEach(t -> traineeStorage.put(t.getId(), t));
                }
                if (data.getTrainers() != null) {
                    data.getTrainers().forEach(t -> trainerStorage.put(t.getId(), t));
                }
                if (data.getTrainings() != null) {
                    data.getTrainings().forEach(t -> trainingStorage.put(t.getId(), t));
                }
            }
            log.info("Initial data loaded: users={}, trainees={}, trainers={}, trainings={}",
                    userStorage.size(), traineeStorage.size(), trainerStorage.size(), trainingStorage.size());
        } catch (IOException e) {
            log.error("Failed to read initial data from {}", initFile, e);
        }
    }

    private InitData readJson(String pathOrClasspath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); // LocalDate მხარდაჭერა
        if (pathOrClasspath.startsWith("classpath:")) {
            String p = pathOrClasspath.substring("classpath:".length());
            Resource res = new ClassPathResource(p);
            if (!res.exists()) return null;
            return mapper.readValue(res.getInputStream(), InitData.class);
        } else {
            Path p = Path.of(pathOrClasspath);
            if (!Files.exists(p)) return null;
            return mapper.readValue(Files.newInputStream(p), InitData.class);
        }
    }

    // DTO for JSON binding
    public static class InitData {
        private java.util.List<User> users;
        private java.util.List<Trainee> trainees;
        private java.util.List<Trainer> trainers;
        private java.util.List<Training> trainings;

        public java.util.List<User> getUsers() { return users; }
        public void setUsers(java.util.List<User> users) { this.users = users; }

        public java.util.List<Trainee> getTrainees() { return trainees; }
        public void setTrainees(java.util.List<Trainee> trainees) { this.trainees = trainees; }

        public java.util.List<Trainer> getTrainers() { return trainers; }
        public void setTrainers(java.util.List<Trainer> trainers) { this.trainers = trainers; }

        public java.util.List<Training> getTrainings() { return trainings; }
        public void setTrainings(java.util.List<Training> trainings) { this.trainings = trainings; }
    }
}
