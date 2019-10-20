package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealsTestData {


    public static final Meal MEAL_OF_USER = new Meal(100002, LocalDateTime.parse("2015-05-30T10:00:00"), "Завтрак", 500);
    public static final List<Meal> MEALS_OF_USER = Arrays.asList(
            new Meal(LocalDateTime.parse("2015-05-30T20:00:00"), "Ужин", 500),
            new Meal(LocalDateTime.parse("2015-05-30T13:00:00"), "Обед", 1000),
            new Meal(LocalDateTime.parse("2015-05-30T10:00:00"), "Завтрак", 500)
    );
}
