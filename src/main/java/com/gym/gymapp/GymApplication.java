package com.gym.gymapp;

import com.gym.gymapp.config.RootConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
public class GymApplication {
	private static final Logger log = LoggerFactory.getLogger(GymApplication.class);

	public static void main(String[] args) {
		try (var ctx = new AnnotationConfigApplicationContext(RootConfig.class)) {
			log.info("Gym CRM started (Spring Core + JPA/Hibernate, no Boot).");
		}
	}
}
