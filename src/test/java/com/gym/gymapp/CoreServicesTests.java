package com.gym.gymapp;

import com.gym.gymapp.model.*;
import com.gym.gymapp.service.TraineeService;
import com.gym.gymapp.service.TrainerService;
import com.gym.gymapp.service.TrainingService;
import com.gym.gymapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CoreServicesTests {

    @Test
    void usernameDedup_and_passwordLen10() {
        try (var ctx = new AnnotationConfigApplicationContext("com.gym.gymapp")) {
            var trainees = ctx.getBean(TraineeService.class);
            var users = ctx.getBean(UserService.class);

            // ორი ერთნაირი სახელის Trainee
            var t1 = new Trainee();
            t1.setDateOfBirth(LocalDate.of(1990, 1, 1));
            t1.setAddress("Addr 1");
            trainees.create("John", "Smith", true, t1);

            var t2 = new Trainee();
            t2.setDateOfBirth(LocalDate.of(1991, 2, 2));
            t2.setAddress("Addr 2");
            trainees.create("John", "Smith", true, t2);

            var list = users.list().stream()
                    .filter(u -> "John".equals(u.getFirstName()) && "Smith".equals(u.getLastName()))
                    .collect(Collectors.toList());

            assertEquals(2, list.size());
            Set<String> usernames = list.stream().map(User::getUsername).collect(Collectors.toSet());
            assertTrue(usernames.contains("john.smith"));
            assertTrue(usernames.contains("john.smith.1"));

            // პაროლის სიგრძე 10

            list.forEach(u -> assertEquals(10, u.getPassword().length()));
        }
    }

    @Test
    void trainee_CRUD() {
        try (var ctx = new AnnotationConfigApplicationContext("com.gym.gymapp")) {
            var trainees = ctx.getBean(TraineeService.class);

            var t = new Trainee();
            t.setDateOfBirth(LocalDate.of(1995, 5, 5));
            t.setAddress("Old");
            trainees.create("Alice", "Brown", true, t);
            assertNotNull(t.getId());

            var got = trainees.get(t.getId());
            assertTrue(got.isPresent());

            t.setAddress("New");
            var updated = trainees.update(t);
            assertEquals("New", updated.getAddress());

            assertTrue(trainees.delete(t.getId()));
            assertTrue(trainees.get(t.getId()).isEmpty());
        }
    }

    @Test
    void training_create_list_and_validation() {
        try (var ctx = new AnnotationConfigApplicationContext("com.gym.gymapp")) {
            var trainees = ctx.getBean(TraineeService.class);
            var trainers = ctx.getBean(TrainerService.class);
            var trainings = ctx.getBean(TrainingService.class);

            // ვქმნით ვალიდურ Trainee/Trainer-ს
            var trn = new Trainer();
            trn.setSpecialization(TrainingType.fitness);
            trainers.create("Bob", "Green", true, trn);
            assertNotNull(trn.getId());

            var tr = new Trainee();
            tr.setDateOfBirth(LocalDate.of(1999, 9, 9));
            tr.setAddress("X");
            trainees.create("Clara", "White", true, tr);
            assertNotNull(tr.getId());

            // სწორი Training
            var training = new Training();
            training.setTraineeId(tr.getId());
            training.setTrainerId(trn.getId());
            training.setTrainingName("Morning Cardio");
            training.setTrainingType(TrainingType.fitness);
            training.setTrainingDate(LocalDate.now());
            training.setTrainingDuration(60);

            trainings.create(training);
            assertNotNull(training.getId());

            assertEquals(1, trainings.listByTrainee(tr.getId()).size());
            assertEquals(1, trainings.listByTrainer(trn.getId()).size());

            // არავალიდური — არარსებული traineeId
            var bad = new Training();
            bad.setTraineeId(9999L);
            bad.setTrainerId(trn.getId());
            bad.setTrainingName("X");
            bad.setTrainingType(TrainingType.fitness);
            bad.setTrainingDate(LocalDate.now());
            bad.setTrainingDuration(30);

            assertThrows(IllegalArgumentException.class, () -> trainings.create(bad));
        }
    }
}
