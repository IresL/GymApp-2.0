package com.gym.gymapp.service;

import com.gym.gymapp.dao.TraineeDao;
import com.gym.gymapp.model.Trainee;
import com.gym.gymapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TraineeService {
    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    private TraineeDao traineeDao;
    private UserService userService;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) { this.traineeDao = traineeDao; }

    @Autowired
    public void setUserService(UserService userService) { this.userService = userService; }

    /** ქმნის User-ს და აკავშირებს Trainee-სთან */
    public Trainee create(String firstName, String lastName, Boolean isActive, Trainee trainee) {
        Objects.requireNonNull(trainee, "trainee is required");
        User u = userService.createUser(firstName, lastName, isActive);
        trainee.setUserId(u.getId());
        traineeDao.save(trainee);
        log.info("Created trainee id={} (userId={})", trainee.getId(), u.getId());
        return trainee;
    }

    public Trainee update(Trainee trainee) {
        Objects.requireNonNull(trainee, "trainee is required");
        if (trainee.getId() == null) throw new IllegalArgumentException("trainee.id is required for update");
        traineeDao.save(trainee);
        log.info("Updated trainee id={}", trainee.getId());
        return trainee;
    }

    public boolean delete(Long id) {
        boolean deleted = traineeDao.delete(id);
        log.info("Deleted trainee id={} -> {}", id, deleted);
        return deleted;
    }

    public Optional<Trainee> get(Long id) { return traineeDao.findById(id); }

    public List<Trainee> list() { return traineeDao.findAll(); }
}
