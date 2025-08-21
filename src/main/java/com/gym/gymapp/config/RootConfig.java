package com.gym.gymapp.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.gym.gymapp") // @Service, @Repository, @Component
@Import(PersistenceConfig.class)                // JPA/Flyway/DataSource
public class RootConfig {}
