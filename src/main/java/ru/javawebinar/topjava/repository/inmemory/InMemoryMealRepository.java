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
import java.util.function.Predicate;
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
        return getAllFitltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return getAllFitltered(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));
    }

    private List<Meal> getAllFitltered(int userId, Predicate<Meal> filter) {
        return (repository.get(userId) == null) ? new ArrayList<>() : repository.get(userId).values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

