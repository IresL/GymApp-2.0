package com.gym.gymapp;

import com.gym.gymapp.facade.GymFacade;
import com.gym.gymapp.model.Trainee;
import com.gym.gymapp.model.Trainer;
import com.gym.gymapp.model.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class GymApplication {
	private static final Logger log = LoggerFactory.getLogger(GymApplication.class);

	public static void main(String[] args) {
		try (var ctx = new AnnotationConfigApplicationContext("com.gym.gymapp")) {
			var facade = ctx.getBean(GymFacade.class);

			// Trainer create
			var trainer = new Trainer();
			trainer.setSpecialization(TrainingType.fitness);
			trainer = facade.createTrainer("John", "Smith", true, trainer);
			log.info("Trainer created id={}, userId={}", trainer.getId(), trainer.getUserId());

			// Trainee create
			var trainee = new Trainee();
			trainee.setDateOfBirth(LocalDate.of(1995, 5, 5));
			trainee.setAddress("Tbilisi");
			trainee = facade.createTrainee("Anna", "Brown", true, trainee);
			log.info("Trainee created id={}, userId={}", trainee.getId(), trainee.getUserId());
		}
	}
}
