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
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal) {

        int userId = meal.getUserId();

        Map<Integer, Meal> userRepository = repository.get(userId);

        if (userRepository == null) {
            userRepository = new ConcurrentHashMap<>();
            repository.put(userId, userRepository);
        }

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userRepository.put(meal.getId(), meal);
            return meal;
        } else {
            Meal result = userRepository.computeIfPresent(meal.getId(),
                    (id, oldMeal) -> Objects.equals(oldMeal.getUserId(), meal.getUserId()) ? meal : oldMeal);
            //cannot return null in the lambda as null in the lambda will remove the key
            return Objects.equals(result, meal) ? meal : null;
        }
    }

    @Override
    public boolean delete(int id, int userID) {
        Map<Integer, Meal> userRepository = repository.get(userID);

        if (userRepository == null) {
            return false;
        }

        Meal meal = get(id, userID);

        if (meal == null) {
            return false;
        }

        return userRepository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userID) {
        log.debug("Get id {}, userID {}", id, userID);

        Map<Integer, Meal> userRepository = repository.get(userID);
        if (userRepository == null) {
            return null;
        }

        Meal meal = userRepository.get(id);

        if (meal == null || meal.getUserId() != userID) {
            return null;
        }

        return meal;
    }

    @Override
    public Collection<Meal> getAllByUserId(int userID) {
        log.debug("getAll id {} items {}", userID, repository.values());
        return (repository.get(userID) == null) ? new ArrayList<>() : repository.get(userID).values();
//                .stream()
//                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime())) //move to controller
//                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return getAllByUserId(userId).stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
//                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime())) //moved to Contrloller level
                .collect(Collectors.toList());
    }


}

