package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal, int userId) {
        log.debug("save Meal {}, userId {}", meal, userId);

        Map<Integer, Meal> userRepository = repository.computeIfAbsent(userId, k -> new HashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userRepository.put(meal.getId(), meal);
            return meal;
        }
        return userRepository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.debug("delete id {}, userId {}", id, userId);

        Map<Integer, Meal> userRepository = repository.get(userId);
        if (userRepository == null) {
            return false;
        }

        Meal meal = get(id, userId);
        if (meal == null) {
            return false;
        }
        return userRepository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.debug("Get id {}, userId {}", id, userId);

        Map<Integer, Meal> userRepository = repository.get(userId);
        if (userRepository == null) {
            return null;
        }

        return userRepository.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.debug("getAll id {} items {}", userId, repository.values());
        return (repository.get(userId) == null) ? new ArrayList<>() : repository.get(userId).values()
                .stream()
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return getAll(userId)
                .stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }
}

