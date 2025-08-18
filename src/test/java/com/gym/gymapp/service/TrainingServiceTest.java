package com.gym.gymapp.service;



import com.gym.gymapp.dao.TraineeDao;
import com.gym.gymapp.dao.TrainerDao;
import com.gym.gymapp.dao.TrainingDao;
import com.gym.gymapp.model.Training;
import com.gym.gymapp.model.TrainingType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingServiceTest {

    @Test
    void create_validatesIds_andSaves() {
        // mocks
        TrainingDao trainingDao = mock(TrainingDao.class);
        TraineeDao traineeDao = mock(TraineeDao.class);
        TrainerDao trainerDao = mock(TrainerDao.class);

        // sut
        TrainingService svc = new TrainingService();
        svc.setTrainingDao(trainingDao);
        svc.setTraineeDao(traineeDao);
        svc.setTrainerDao(trainerDao);

        // existing ids
        when(traineeDao.findById(1L)).thenReturn(Optional.of(new com.gym.gymapp.model.Trainee()));
        when(trainerDao.findById(2L)).thenReturn(Optional.of(new com.gym.gymapp.model.Trainer()));
        when(trainingDao.save(any())).thenAnswer(inv -> {
            Training tr = inv.getArgument(0);
            tr.setId(100L);
            return tr;
        });

        Training ok = new Training();
        ok.setTraineeId(1L);
        ok.setTrainerId(2L);
        ok.setTrainingName("Morning");
        ok.setTrainingType(TrainingType.fitness);
        ok.setTrainingDate(LocalDate.now());
        ok.setTrainingDuration(60);

        Training saved = svc.create(ok);
        assertNotNull(saved.getId());

        // invalid trainee
        when(traineeDao.findById(999L)).thenReturn(Optional.empty());
        Training bad = new Training();
        bad.setTraineeId(999L);
        bad.setTrainerId(2L);
        bad.setTrainingName("X");
        bad.setTrainingType(TrainingType.fitness);
        bad.setTrainingDate(LocalDate.now());
        bad.setTrainingDuration(10);

        assertThrows(IllegalArgumentException.class, () -> svc.create(bad));
    }
}

