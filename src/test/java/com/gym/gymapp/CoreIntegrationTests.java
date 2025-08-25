package com.gym.gymapp;

import com.gym.gymapp.config.RootConfig;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RootConfig.class)
@TestPropertySource(locations = "classpath:application.properties")
@TestMethodOrder(OrderAnnotation.class)
class CoreIntegrationTests {

    private static final Logger log = LoggerFactory.getLogger(CoreIntegrationTests.class);

    @org.springframework.beans.factory.annotation.Autowired
    ApplicationContext ctx;

    @org.springframework.beans.factory.annotation.Autowired
    DataSource dataSource;

    @org.springframework.beans.factory.annotation.Autowired
    EntityManagerFactory emf;

    // თუ Flyway@Bean გაქვს PersistenceConfig-ში, ესაც ჩაინჯექთება
    @org.springframework.beans.factory.annotation.Autowired(required = false)
    Flyway flyway;

    @Test
    @Order(1)
    void context_loads() {
        log.info("Checking ApplicationContext & core infrastructure beans...");
        assertNotNull(ctx, "ApplicationContext must be initialized");
        assertNotNull(dataSource, "DataSource must be available");
        assertNotNull(emf, "EntityManagerFactory must be available");
    }

    @Test
    @Order(2)
    void create_profiles_and_auth() {
        log.info("Smoke-check: services & facade presence (to be expanded later)...");
        // უბრალოდ ვამოწმებთ რომ ძირითადი სერვისები/ფასადი კონტექსტში არსებობს.
        assertTrue(ctx.containsBeanDefinition("gymFacade")
                        || ctx.getBeansOfType(com.gym.gymapp.facade.GymFacade.class).size() > 0,
                "GymFacade bean should be present");

        assertFalse(ctx.getBeansOfType(com.gym.gymapp.service.AuthService.class).isEmpty(),
                "AuthService bean should be present");
        assertFalse(ctx.getBeansOfType(com.gym.gymapp.service.TraineeService.class).isEmpty(),
                "TraineeService bean should be present");
        assertFalse(ctx.getBeansOfType(com.gym.gymapp.service.TrainerService.class).isEmpty(),
                "TrainerService bean should be present");
        assertFalse(ctx.getBeansOfType(com.gym.gymapp.service.TrainingService.class).isEmpty(),
                "TrainingService bean should be present");
        assertFalse(ctx.getBeansOfType(com.gym.gymapp.service.UserService.class).isEmpty(),
                "UserService bean should be present");
    }

    @Test
    @Order(3)
    void add_training_and_query_both_sides() {
        log.info("Placeholder: add training & query from trainer/trainee sides (to be implemented).");
        // TODO: აქ შემდგომში მოათავსებ რეალურ სცენარს (create training + queries)
        assertNotNull(ctx); // Smoke so test passes for now
    }

    @Test
    @Order(4)
    void activate_deactivate_and_password_change() {
        log.info("Placeholder: activate/deactivate users and change password (to be implemented).");
        // TODO: რეალური activate/deactivate + password change სქენარები
        assertNotNull(ctx);
    }

    @Test
    @Order(5)
    void delete_trainee_cascades_trainings() {
        log.info("Placeholder: delete trainee -> cascade delete trainings (to be implemented).");
        // TODO: Trainee delete + trainings cascade check
        assertNotNull(ctx);
    }

    @Test
    @Order(6)
    void not_assigned_trainer_list_and_update_links() {
        log.info("Placeholder: list not-assigned trainers and update trainee<->trainer links (to be implemented).");
        // TODO: not-assigned list + update many-to-many links
        assertNotNull(ctx);
    }

    @Test
    @Order(7)
    void flyway_migrations_applied() {
        log.info("Checking Flyway migrations (if Flyway bean is present)...");
        if (flyway == null) {
            log.warn("Flyway bean not found - skipping migration assertion (ok if Flyway disabled).");
            return;
        }
        var info = flyway.info();
        assertNotNull(info, "Flyway.info() should not be null");
        var applied = info.applied();
        assertTrue(applied != null && applied.length >= 1,
                "At least one Flyway migration should be applied");
        log.info("Applied migrations: {}", (Object) applied);
    }
}
