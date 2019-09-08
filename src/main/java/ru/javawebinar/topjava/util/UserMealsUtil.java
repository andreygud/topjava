package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 12, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 700/*2000*/));
//        .toLocalDate();
//        .toLocalTime();
        System.out.println(getFilteredWithExceededOptional1(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 700/*2000*/));
    }


    //Time Complexity O(N) because there is only one for cycle.

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> result = new ArrayList<>();

        for (UserMeal meal : mealList) {
            LocalTime mealTime = meal.getDateTime().toLocalTime();
            if (mealTime.isAfter(startTime) && mealTime.isBefore(endTime)) {
                int mealCalories = meal.getCalories();
                result.add(new UserMealWithExceed(meal, mealCalories > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededOptional1(List<UserMeal> mealList, LocalTime
            startTime, LocalTime endTime, int caloriesPerDay) {

        return mealList.stream().filter(
                mealitem -> {
                    LocalTime time = mealitem.getDateTime().toLocalTime();
                    return time.isAfter(startTime) && time.isBefore(endTime);
                }).map(meal -> new UserMealWithExceed(meal, meal.getCalories() > caloriesPerDay)).collect(Collectors.toList());

    }
}
