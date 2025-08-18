package com.gym.gymapp.util;


import com.gym.gymapp.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsernameGeneratorTest {

    @Test
    void generatesUniqueUsernamesUsingDaoExistenceChecks() {
        UserDao userDao = Mockito.mock(UserDao.class);
        UsernameGenerator gen = new UsernameGenerator();
        gen.setUserDao(userDao);

        // base თავისუფალია
        when(userDao.existsByUsername("Ciaphas.Cain")).thenReturn(false);
        assertEquals("Ciaphas.Cain", gen.generate("Ciaphas", "Cain"));

        // base დაკავებულია, .1 დაკავებულია, .2 თავისუფალია
        when(userDao.existsByUsername("Ciaphas.Cain")).thenReturn(true);
        when(userDao.existsByUsername("Ciaphas.Cain.1")).thenReturn(true);
        when(userDao.existsByUsername("Ciaphas.Cain.2")).thenReturn(false);
        assertEquals("Ciaphas.Cain.2", gen.generate("Ciaphas", "Cain"));

        verify(userDao, atLeastOnce()).existsByUsername(anyString());
    }
}
