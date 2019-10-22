package ru.javawebinar.topjava;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealsTestData {
    public static final Meal MEAL_OF_USER = new Meal(100002, LocalDateTime.of(2015,5,30,10,0), "Завтрак", 500);
    public static final Meal MEAL_OF_USER1 = new Meal(100003, LocalDateTime.of(2015,5,30,13,0), "Обед", 1000);
    public static final Meal MEAL_OF_USER2 = new Meal(100004, LocalDateTime.of(2015,5,30,20,0), "Ужин", 500);
    public static final Meal MEAL_OF_USER3 = new Meal(100005, LocalDateTime.of(2019,10,23,9,0), "Завтрак", 500);
    public static final Meal MEAL_OF_USER4 = new Meal(100006, LocalDateTime.of(2019,10,23,12,0), "Обед", 1500);
    public static final Meal MEAL_OF_USER5 = new Meal(100007, LocalDateTime.of(2019,10,24,9,0), "Завтрак", 1500);
    public static final Meal MEAL_OF_USER_CREATED = new Meal(100011, LocalDateTime.of(2019,10,10,10,12), "Завтрак", 500);

    public static final Meal MEAL_OF_ADMIN = new Meal(100008, LocalDateTime.of(2015,5,31,10,0), "Завтрак", 1000);
    public static final Meal MEAL_OF_ADMIN1 = new Meal(100009, LocalDateTime.of(2015,5,31,13,0), "Обед", 500);
    public static final Meal MEAL_OF_ADMIN2 = new Meal(100010, LocalDateTime.of(2015,5,31,20,0), "Ужин", 510);
    public static final Meal MEAL_OF_ADMIN_AFTER_CREATE = new Meal(100011, LocalDateTime.of(2015,5,30,10,0), "Завтрак", 500);

    public static final List<Meal> MEALS_OF_USER_ALL = Arrays.asList(
            MEAL_OF_USER5,
            MEAL_OF_USER4,
            MEAL_OF_USER3,
            MEAL_OF_USER2,
            MEAL_OF_USER1,
            MEAL_OF_USER
    );
    public static final List<Meal> MEALS_OF_USER_AFTER_CREATE = Arrays.asList(
            MEAL_OF_USER5,
            MEAL_OF_USER4,
            MEAL_OF_USER3,
            MEAL_OF_USER_CREATED,
            MEAL_OF_USER2,
            MEAL_OF_USER1,
            MEAL_OF_USER
    );
    public static final List<Meal> MEALS_OF_USER_AFTER_DELETE = Arrays.asList(
            MEAL_OF_USER5,
            MEAL_OF_USER4,
            MEAL_OF_USER3,
            MEAL_OF_USER2,
            MEAL_OF_USER1
    );
    public static final List<Meal> MEALS_OF_USER_FOR_DATES = Arrays.asList(
            MEAL_OF_USER5,
            MEAL_OF_USER4,
            MEAL_OF_USER3
    );


    public static final List<Meal> MEALS_OF_ADMIN_ALL = Arrays.asList(
            MEAL_OF_ADMIN2,
            MEAL_OF_ADMIN1,
            MEAL_OF_ADMIN
    );
    public static final List<Meal> MEALS_OF_ADMIN_AFTER_CREATE = Arrays.asList(
            MEAL_OF_ADMIN2,
            MEAL_OF_ADMIN1,
            MEAL_OF_ADMIN,
            MEAL_OF_ADMIN_AFTER_CREATE
    );


    public static void assertMatch(List<Meal> actual, List<Meal> expected) {
        Assertions.assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}
