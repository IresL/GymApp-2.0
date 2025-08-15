package com.gym.gymapp;

import com.gym.gymapp.config.AppConfig;
import com.gym.gymapp.facade.GymFacade;
import com.gym.gymapp.model.TrainingType;
import com.gym.gymapp.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class GymApplication {
	private static final Logger logger = LoggerFactory.getLogger(GymApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Gym CRM Application");

		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		GymFacade facade = context.getBean(GymFacade.class);

		// Demo usage
		try {
			// Create a trainee
			User trainee = facade.createTrainee("John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St");
			logger.info("Created trainee: {} with password: {}", trainee.getUsername(), trainee.getPassword());

			// Create a trainer
			User trainer = facade.createTrainer("Jane", "Smith", TrainingType.FITNESS);
			logger.info("Created trainer: {} with password: {}", trainer.getUsername(), trainer.getPassword());

			logger.info("Gym CRM Application demo completed successfully");
		} catch (Exception e) {
			logger.error("Error during application demo", e);
		}
	}
}
