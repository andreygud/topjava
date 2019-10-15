package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal) {

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but not present in storage
        Meal result = repository.computeIfPresent(meal.getId(),
                (id, oldMeal) -> Objects.equals(oldMeal.getUserId(), meal.getUserId()) ? meal : oldMeal);

        //this check is needed because, if return null is inside computeIfPresent - the key is removed
        return Objects.equals(result, meal) ? result : null;
    }

    @Override
    public boolean delete(int id, int userID) {
        Meal meal = get(id, userID);

        if (meal == null) {
            return false;
        }
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userID) {
        log.debug("Get id {}, userID {}", id, userID);
        Meal meal = repository.get(id);

        if (meal == null) {
            return null;
        } else if (meal.getUserId() != userID) {
            return null;
        }

        return meal;
    }

    @Override
    public List<Meal> getAll() {
        log.debug("getAll items {} , size {}", repository, repository.size());
        return repository.values().stream()
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllByUserId(int userID) {
        log.debug("getAll id {} items {}", userID, repository.values());
        return getAll().stream()
                .filter(meal -> (meal.getUserId() == userID))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllByTimeBoundaries(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return getAllByUserId(userId).stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }

    @Override
    public Map<LocalDate, Integer> getCaloriesSumByDate(int userId) {
        return repository.values().stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
    }
}

