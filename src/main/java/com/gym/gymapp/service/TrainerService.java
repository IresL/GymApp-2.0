package com.gym.gymapp.service;

import com.gym.gymapp.dao.TrainerDao;
import com.gym.gymapp.model.Trainer;
import com.gym.gymapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TrainerService {
    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDao trainerDao;
    private UserService userService;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) { this.trainerDao = trainerDao; }

    @Autowired
    public void setUserService(UserService userService) { this.userService = userService; }

    /** ქმნის User-ს და აკავშირებს Trainer-სთან (specialization უნდა იყოს უკვე დადგენილი ობიექტში) */
    public Trainer create(String firstName, String lastName, Boolean isActive, Trainer trainer) {
        Objects.requireNonNull(trainer, "trainer is required");
        if (trainer.getSpecialization() == null) throw new IllegalArgumentException("trainer.specialization is required");
        User u = userService.createUser(firstName, lastName, isActive);
        trainer.setUserId(u.getId());
        trainerDao.save(trainer);
        log.info("Created trainer id={} (userId={})", trainer.getId(), u.getId());
        return trainer;
    }

    public Trainer update(Trainer trainer) {
        Objects.requireNonNull(trainer, "trainer is required");
        if (trainer.getId() == null) throw new IllegalArgumentException("trainer.id is required for update");
        trainerDao.save(trainer);
        log.info("Updated trainer id={}", trainer.getId());
        return trainer;
    }

    public Optional<Trainer> get(Long id) { return trainerDao.findById(id); }

    public List<Trainer> list() { return trainerDao.findAll(); }
}
