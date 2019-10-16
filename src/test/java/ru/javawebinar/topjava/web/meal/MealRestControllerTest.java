package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealRestControllerTest {

    private MealRestController mealRestController;

    @BeforeEach
    void setUp() {
        InMemoryMealRepository mealRepository = new InMemoryMealRepository();
        List<Meal> mealsWithUserID = Arrays.asList(
                new Meal(LocalDateTime.of(2018, Month.APRIL, 1, 9, 0), "Ужин", 510, 1),
                new Meal(LocalDateTime.of(2018, Month.APRIL, 2, 11, 0), "Ужин", 510, 1),
                new Meal(LocalDateTime.of(2018, Month.APRIL, 3, 13, 0), "Ужин", 510, 1),
                new Meal(LocalDateTime.of(2018, Month.APRIL, 4, 15, 0), "Ужин", 510, 1),
                new Meal(LocalDateTime.of(2018, Month.APRIL, 1, 17, 0), "Ужин", 510, 1)
        );
        mealsWithUserID.forEach(mealRepository::save);
        MealService mealService = new MealService(mealRepository);
        mealRestController = new MealRestController(mealService);

    }

    @Test
    void getAllByTimeBoundaries() {

        //get all - all boundaries null
        assertEquals(5, mealRestController.getAllByTimeBoundaries(
                null,
                null,
                null,
                null).size());

    }
}