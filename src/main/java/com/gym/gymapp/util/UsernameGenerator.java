package com.gym.gymapp.util;

import com.gym.gymapp.dao.UserDao;
import com.gym.gymapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Username გენერატორი:
 * - ფორმატი: first.last (lowercase, space-ები მოცილებული)
 * - დუპლიკატის შემთხვევაში: first.last.1, first.last.2, ...
 */
@Component
public class UsernameGenerator {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String generate(String firstName, String lastName) {
        String base = (firstName + "." + lastName)
                .trim()
                .replaceAll("\\s+", "")
                .toLowerCase();

        // თუ თვითონ base თავისუფალია, ვაბრუნებთ მასვე
        if (!userDao.existsByUsername(base)) {
            return base;
        }

        // სხვაგვარად ვპოულობთ პირველ თავისუფალ ".N" ვერსიას
        int i = 1;
        String candidate;
        do {
            candidate = base + "." + i++;
        } while (userDao.existsByUsername(candidate));
        return candidate;
    }
}
       /* Set<String> existing = existingUsernames();
        String candidate = base;
        int counter = 1;

        while (existing.contains(candidate)) {
            candidate = base + "." + counter;
            counter++;
        }
        return candidate;
    }

    private Set<String> existingUsernames() {
        Set<String> set = new HashSet<>();
        for (User u : userDao.findAll()) {
            if (u.getUsername() != null) {
                set.add(u.getUsername());
            }
        }
        return set;
    }
}*/
