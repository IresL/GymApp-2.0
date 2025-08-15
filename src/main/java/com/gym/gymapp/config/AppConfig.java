package com.gym.gymapp.config;

import com.gym.gymapp.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;



 // Declares separate in-memory storage beans for each entity type.

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean(name = "userStorage")
    public Map<Long, User> userStorage() {
        return new HashMap<>();
    }

    @Bean(name = "traineeStorage")
    public Map<Long, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean(name = "trainerStorage")
    public Map<Long, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean(name = "trainingStorage")
    public Map<Long, Training> trainingStorage() {
        return new HashMap<>();
    }
}