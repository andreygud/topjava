package ru.javawebinar.topjava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.MealServlet;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealServiceTest {

    MealService mealService;

    @BeforeEach
    void setUp() {
        InMemoryMealRepository mealRepository = new InMemoryMealRepository();
        List<Meal> mealsWithUserID = Arrays.asList(
                new Meal(LocalDateTime.of(2018, Month.APRIL, 15, 20, 0), "Ужин", 510, 2)
        );
        mealsWithUserID.forEach(mealRepository::save);
        mealService = new MealService(mealRepository);
    }

    @Test
    void update() {
        //check that user cannot update records he doesn't own
        assertNotNull(mealService.get(1, 2));
        Meal meal = new Meal(1, LocalDateTime.of(2030, Month.MAY, 15, 20, 0), "Lunch", 1200, 1);
        assertThrows(NotFoundException.class, () -> mealService.update(meal));
    }

    @Test
    void get() {
        assertNotNull(mealService.get(1, 2));
        assertThrows(NotFoundException.class, () -> mealService.get(1, 1));
    }

    @Test
    void delete() {
        assertNotNull(mealService.get(1, 2));
        assertThrows(NotFoundException.class, () -> mealService.delete(1, 1));
        assertDoesNotThrow(() -> mealService.delete(1, 2));
        assertEquals(0, mealService.getAllByAuthUser(2).size());
    }
}