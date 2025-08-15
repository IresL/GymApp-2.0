package com.gym.gymapp.dao;

import com.gym.gymapp.model.User;
import com.gym.gymapp.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    private static final String NAMESPACE = "users";

    private InMemoryStorage storage;

    @Autowired
    public void setStorage(InMemoryStorage storage) {
        this.storage = storage;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(storage.generateId(NAMESPACE));
        }
        storage.save(NAMESPACE, user.getId(), user);
        logger.info("Saved user: {}", user.getUsername());
        return user;
    }

    public Optional<User> findById(Long id) {
        User user = storage.findById(NAMESPACE, id);
        logger.debug("Finding user by id {}: {}", id, user != null ? "found" : "not found");
        return Optional.ofNullable(user);
    }

    public Optional<User> findByUsername(String username) {
        Map<Long, User> users = storage.getNamespace(NAMESPACE);
        Optional<User> user = users.values().stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst();
        logger.debug("Finding user by username {}: {}", username, user.isPresent() ? "found" : "not found");
        return user;
    }

    public Collection<User> findAll() {
        Collection<User> users = storage.<User>getNamespace(NAMESPACE).values();
        logger.debug("Finding all users: {} found", users.size());
        return users;
    }

    public boolean delete(Long id) {
        boolean deleted = storage.delete(NAMESPACE, id);
        logger.info("Deleted user with id {}: {}", id, deleted);
        return deleted;
    }
}
