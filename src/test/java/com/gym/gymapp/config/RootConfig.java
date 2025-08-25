package com.gym.gymapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PersistenceConfig.class)          // ჩვენი პროდაქშენ კონფიგი
@ComponentScan(basePackages = "com.gym.gymapp") // services, util, facade, וכו.
public class RootConfig { }

