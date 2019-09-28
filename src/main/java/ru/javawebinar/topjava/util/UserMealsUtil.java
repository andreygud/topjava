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
                        HashMap<LocalDate, MyValueObject<Integer, List<UserMeal>>>::new,
                        (acc, meal) -> {
                            LocalDate key = meal.getDateTime().toLocalDate();
                            MyValueObject<Integer, List<UserMeal>> valueObj = acc.get(key);
                            if (valueObj == null) {
                                valueObj = new MyValueObject<>(0, new ArrayList<>());
                            }

                            valueObj.getField2().add(meal);
                            int currentSum = valueObj.getField1();

                            valueObj.setField1(currentSum + meal.getCalories());

                            acc.put(key, valueObj);
                        },
                        (part1, part2) ->
                        {
                            for (Map.Entry<LocalDate, MyValueObject<Integer, List<UserMeal>>> entry : part2.entrySet()) {

                                part1.merge(entry.getKey(), entry.getValue(), (v1, v2) -> {
                                    v1.setField1(v1.getField1() + v2.getField1());
                                    v1.getField2().addAll(v2.getField2());
                                    return v1;
                                });
                            }
                            return part1;
                        }
                )).entrySet().stream()
                .collect(Collector.of(
                        ArrayList::new,
                        (List<UserMealWithExceed> acc, Map.Entry<LocalDate, MyValueObject<Integer, List<UserMeal>>> mapEntry) -> {

                            int caloriesSummary = mapEntry.getValue().getField1();

                            List<UserMealWithExceed> mealWithExceedList = mapEntry.getValue().getField2().stream()
                                    .map(meal -> new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesSummary > caloriesPerDay))
                                    .collect(toList());

                            acc.addAll(mealWithExceedList);
                        }
                        , (part1, part2) -> {
                            part1.addAll(part2);
                            return part1;
                        })).stream()
                .filter(item -> TimeUtil.isBetween(item.getDateTime().toLocalTime(), startTime, endTime))
                .collect(toList());
    }

    static class MyValueObject<A, B> {
        private A field1;
        private B field2;

        public MyValueObject(A field1, B field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public A getField1() {
            return field1;
        }

        public void setField1(A field1) {
            this.field1 = field1;
        }

        public B getField2() {
            return field2;
        }

        public void setField2(B field2) {
            this.field2 = field2;
        }
    }

}
