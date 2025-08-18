package com.gym.gymapp.service;



import com.gym.gymapp.dao.TraineeDao;
import com.gym.gymapp.model.Trainee;
import com.gym.gymapp.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;

public class TraineeServiceTest {

    @Test
    void create_callsUserServiceAndSavesTraineeWithUserId() {
        // mocks
        TraineeDao traineeDao = mock(TraineeDao.class);
        UserService userService = mock(UserService.class);

        // sut
        TraineeService svc = new TraineeService();
        svc.setTraineeDao(traineeDao);
        svc.setUserService(userService);

        // stubs
        User created = new User();
        created.setId(42L);
        when(userService.createUser("Tona", "Creed", true)).thenReturn(created);
        when(traineeDao.save(any())).thenAnswer(inv -> {
            Trainee t = inv.getArgument(0);
            t.setId(7L);
            return t;
        });

        // act
        Trainee t = new Trainee();
        t.setDateOfBirth(LocalDate.of(1995,5,5));
        t.setAddress("Tbilisi");
        Trainee res = svc.create("Tona", "Creed", true, t);

        // assert
        assertEquals(7L, res.getId());
        assertEquals(42L, res.getUserId());
        verify(userService).createUser("Tona", "Creed", true);
        verify(traineeDao).save(any(Trainee.class));
    }
}
