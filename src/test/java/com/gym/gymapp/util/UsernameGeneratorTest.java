package com.gym.gymapp.util;

import com.gym.gymapp.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsernameGeneratorTest {

    @Test
    void generates_unique_usernames_with_increment() {
        UserRepository repo = mock(UserRepository.class);
        UsernameGenerator gen = new UsernameGenerator(repo);

        // "john.smith" თავისუფალია
        when(repo.existsByUsernameIgnoreCase("john.smith")).thenReturn(false);
        assertEquals("john.smith", gen.generate("John", "Smith"));

        // უკვე დაკავებულია base და .1; თავისუფალია .2
        when(repo.existsByUsernameIgnoreCase("john.smith")).thenReturn(true);
        when(repo.existsByUsernameIgnoreCase("john.smith.1")).thenReturn(true);
        when(repo.existsByUsernameIgnoreCase("john.smith.2")).thenReturn(false);
        assertEquals("john.smith.2", gen.generate("John", "Smith"));

        verify(repo, atLeastOnce()).existsByUsernameIgnoreCase(anyString());
    }
}

