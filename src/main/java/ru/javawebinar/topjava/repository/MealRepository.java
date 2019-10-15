package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Meal get(int id, int userId);

    List<Meal> getAll();

    List<Meal> getAllByUserId(int userId);

    Map<LocalDate, Integer> getCaloriesSumByDate(int userId);

    public List<Meal> getAllByTimeBoundaries(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);
}