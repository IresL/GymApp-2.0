package com.gym.gymapp.dao;

import com.gym.gymapp.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDao {

    private final Map<Long, User> storage;
    private long seq = 1L;

    public UserDao(@Qualifier("userStorage") Map<Long, User> storage) {
        this.storage = storage;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(seq++);
        }
        storage.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return storage.values().stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    //checker
    public boolean existsByUsername(String username) {
        if (username == null) return false;
        return storage.values().stream().anyMatch(u -> username.equals(u.getUsername()));
    }
}