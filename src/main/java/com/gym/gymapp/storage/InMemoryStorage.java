package com.gym.gymapp.storage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;

@Component
public class InMemoryStorage implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryStorage.class);

    private final Map<String, Map<Long, Object>> storage = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> idCounters = new ConcurrentHashMap<>();

    @Value("${storage.init.file.path:}")
    private String initFilePath;

    public InMemoryStorage() {
        storage.put("users", new ConcurrentHashMap<>());
        storage.put("trainees", new ConcurrentHashMap<>());
        storage.put("trainers", new ConcurrentHashMap<>());
        storage.put("trainings", new ConcurrentHashMap<>());

        idCounters.put("users", new AtomicLong(0));
        idCounters.put("trainees", new AtomicLong(0));
        idCounters.put("trainers", new AtomicLong(0));
        idCounters.put("trainings", new AtomicLong(0));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (initFilePath != null && !initFilePath.isEmpty()) {
            loadInitialData();
        } else {
            logger.info("No initialization file specified, starting with empty storage");
        }
    }

    private void loadInitialData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(initFilePath))) {
            logger.info("Loading initial data from: {}", initFilePath);
            String line;
            while ((line = reader.readLine()) != null) {
                logger.debug("Processing line: {}", line);
                // Here you would parse the file and populate initial data
                // For this example, we'll just log that we're loading
            }
            logger.info("Initial data loaded successfully");
        } catch (IOException e) {
            logger.warn("Could not load initial data from file: {}", initFilePath, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Map<Long, T> getNamespace(String namespace) {
        return (Map<Long, T>) storage.get(namespace);
    }

    public Long generateId(String namespace) {
        return idCounters.get(namespace).incrementAndGet();
    }

    public <T> void save(String namespace, Long id, T entity) {
        getNamespace(namespace).put(id, entity);
        logger.debug("Saved entity with id {} in namespace {}", id, namespace);
    }

    public <T> T findById(String namespace, Long id) {
        T entity = (T) getNamespace(namespace).get(id);
        logger.debug("Found entity with id {} in namespace {}: {}", id, namespace, entity != null);
        return entity;
    }

    public <T> boolean delete(String namespace, Long id) {
        T removed = (T) getNamespace(namespace).remove(id);
        logger.debug("Deleted entity with id {} in namespace {}: {}", id, namespace, removed != null);
        return removed != null;
    }
}

