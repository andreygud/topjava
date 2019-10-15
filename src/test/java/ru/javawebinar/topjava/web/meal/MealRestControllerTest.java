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

        //get by Time - both boundaries specified
        assertEquals(2, mealRestController.getAllByTimeBoundaries(
                null,
                null,
                "11:00",
                "13:00").size());

        //get by Time - one boundari specified - start
        //get by Time - one boundari specified - end


        //get by Date - both boundaries specified
        assertEquals(2, mealRestController.getAllByTimeBoundaries(
                "2018-04-01",
                "2018-04-01",
                "",
                "").size());

        //get by Date - one boundari specified - start
        //get by Date - one boundari specified - end

        //get by Date and Time - all fields specified
        assertEquals(3, mealRestController.getAllByTimeBoundaries(
                "2018-04-02",
                "2018-04-05",
                "11:00",
                "15:00").size());


        //try to pass Date Time in inverted order
        assertEquals(0, mealRestController.getAllByTimeBoundaries(
                "2018-04-05",
                "2018-04-02",
                "15:00",
                "11:00").size());


        //specify wrong value in the Date/Time fields
        assertThrows(DateTimeParseException.class, () -> mealRestController.getAllByTimeBoundaries(
                "2AA018-04-05",
                null,
                null,
                null));
    }
}