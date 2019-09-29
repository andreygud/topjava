package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceededOptional1(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(getFilteredWithExceededOptional2(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

    }


    //Time Complexity O(N) because there is only one for cycle.

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> calSumMap = new HashMap<>();

        for (UserMeal meal : mealList) {
            LocalDate date = meal.getDateTime().toLocalDate();
            calSumMap.merge(date, meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExceed> result = new ArrayList<>();

        for (UserMeal meal : mealList) {
            LocalDateTime dateTime = meal.getDateTime();

            if (TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime)) {
                LocalDate date = dateTime.toLocalDate();
                result.add(new UserMealWithExceed(dateTime, meal.getDescription(), meal.getCalories(), calSumMap.get(date) > caloriesPerDay));
            }
        }

        return result;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededOptional1(List<UserMeal> mealList, LocalTime
            startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesSummary = mealList.stream()
                .collect(Collectors.toMap(
                        meal -> meal.getDateTime().toLocalDate(),
                        UserMeal::getCalories, Integer::sum));


        return mealList.stream()
                .filter(item -> TimeUtil.isBetween(item.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> {
                    LocalDate date = meal.getDateTime().toLocalDate();
                    return new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesSummary.get(date) > caloriesPerDay);
                }).collect(Collectors.toList());

    }


    public static List<UserMealWithExceed> getFilteredWithExceededOptional2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return mealList.stream()
                .collect(Collector.of(
                        HashMap<LocalDate, MyValueObject>::new,
                        (acc, meal) -> {
                            LocalDate key = meal.getDateTime().toLocalDate();
                            MyValueObject valueObj = acc.computeIfAbsent(key, (k) -> new MyValueObject(0, new ArrayList<>()));

                            valueObj.getMealList().add(meal);
                            valueObj.setCaloriesSummary(valueObj.getCaloriesSummary() + meal.getCalories());

                            acc.put(key, valueObj);
                        },
                        (part1, part2) -> {
                            throw new IllegalArgumentException("Combiner Executed on a sequential stream");
                        }
                )).entrySet().stream()
                .collect(Collector.of(
                        ArrayList::new,
                        (List<UserMealWithExceed> acc, Map.Entry<LocalDate, MyValueObject> mapEntry) -> {

                            int caloriesSummary = mapEntry.getValue().getCaloriesSummary();

                            List<UserMealWithExceed> mealWithExceedList = mapEntry.getValue().getMealList().stream()
                                    .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesSummary > caloriesPerDay))
                                    .collect(toList());

                            acc.addAll(mealWithExceedList);
                        }
                        , (part1, part2) -> {
                            throw new IllegalArgumentException("Combiner Executed on a sequential stream");
                        })).stream()
                .filter(item -> TimeUtil.isBetween(item.getDateTime().toLocalTime(), startTime, endTime))
                .collect(toList());
    }

    static class MyValueObject {
        private Integer caloriesSummary;
        private List<UserMeal> mealList;

        public MyValueObject(Integer caloriesSummary, List<UserMeal> mealList) {
            this.caloriesSummary = caloriesSummary;
            this.mealList = mealList;
        }

        public Integer getCaloriesSummary() {
            return caloriesSummary;
        }

        public void setCaloriesSummary(Integer caloriesSummary) {
            this.caloriesSummary = caloriesSummary;
        }

        public List<UserMeal> getMealList() {
            return mealList;
        }

        public void setMealList(List<UserMeal> mealList) {
            this.mealList = mealList;
        }
    }

}
