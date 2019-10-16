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
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private Map<Integer, Set<Integer>> userIndex = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    synchronized public Meal save(Meal meal) {

        Meal result = null;

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            result = meal;
        } else {
            result = repository.computeIfPresent(meal.getId(),
                    (id, oldMeal) -> Objects.equals(oldMeal.getUserId(), meal.getUserId()) ? meal : oldMeal);
            //cannot return null in the lambda as null in the lambda will remove the key
        }

        //this Objects.equals check is needed because, if return null is inside computeIfPresent - the key is removed
        if (Objects.equals(result, meal)) {
            Set<Integer> mealSet = userIndex.get(meal.getUserId());
            if (mealSet == null) {
                mealSet = new CopyOnWriteArraySet<>();
                userIndex.put(meal.getUserId(), mealSet);
            }
            mealSet.add(meal.getId());
        }

        return result;
    }

    @Override
    synchronized public boolean delete(int id, int userID) {
        Meal meal = get(id, userID);

        if (meal == null) {
            return false;
        }

        if (repository.remove(id) != null) {

            Set<Integer> mealSet = userIndex.get(meal.getUserId());

            return mealSet.remove(meal.getId());
        }
        return false;
    }

    @Override
    public Meal get(int id, int userID) {
        log.debug("Get id {}, userID {}", id, userID);
        Meal meal = repository.get(id);

        if (meal == null || meal.getUserId() != userID) {
            return null;
        }
        return meal;
    }

    @Override
    public Collection<Meal> getAllByUserId(int userID) {
        log.debug("getAll id {} items {}", userID, repository.values());
        return userIndex.get(userID).stream()
                .map(mealId -> repository.get(mealId))
//                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime())) //move to controller
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return userIndex.get(userId).stream()
                .map(mealId -> repository.get(mealId))
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
//                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime())) //moved to Contrloller level
                .collect(Collectors.toList());
    }


}

