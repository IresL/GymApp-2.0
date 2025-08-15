package com.gym.gymapp.service;

import com.gym.gymapp.dao.UserDao;
import com.gym.gymapp.model.User;
import com.gym.gymapp.util.PasswordGenerator;
import com.gym.gymapp.util.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserDao userDao;
    private UsernameGenerator usernameGenerator;
    private PasswordGenerator passwordGenerator;

    @Autowired
    public void setUserDao(UserDao userDao) { this.userDao = userDao; }

    @Autowired
    public void setUsernameGenerator(UsernameGenerator usernameGenerator) { this.usernameGenerator = usernameGenerator; }

    @Autowired
    public void setPasswordGenerator(PasswordGenerator passwordGenerator) { this.passwordGenerator = passwordGenerator; }

    public User createUser(String firstName, String lastName, Boolean isActive) {
        Objects.requireNonNull(firstName, "firstName is required");
        Objects.requireNonNull(lastName, "lastName is required");
        if (isActive == null) isActive = Boolean.TRUE;

        String username = usernameGenerator.generate(firstName, lastName);
        String password = passwordGenerator.generate(10);

        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setUsername(username);
        u.setPassword(password);
        u.setIsActive(isActive);

        userDao.save(u);
        log.info("Created user id={}, username={}", u.getId(), u.getUsername());
        return u;
    }

    public Optional<User> get(Long id) { return userDao.findById(id); }

    public Optional<User> findByUsername(String username) { return userDao.findByUsername(username); }

    public List<User> list() { return userDao.findAll(); }

    public boolean delete(Long id) { return userDao.delete(id); }
}
