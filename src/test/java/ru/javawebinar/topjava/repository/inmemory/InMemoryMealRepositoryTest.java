package ru.javawebinar.topjava.repository.inmemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryMealRepositoryTest {

    private InMemoryMealRepository mealRepository;

    @BeforeEach
    void setUp() {

        mealRepository = new InMemoryMealRepository();

        List<Meal> mealsWithUserID = Arrays.asList(
                new Meal(LocalDateTime.of(2019, Month.MAY, 31, 9, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2019, Month.MAY, 31, 13, 0), "Обед", 510),
                new Meal(LocalDateTime.of(2019, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        mealsWithUserID.forEach(meal -> mealRepository.save(meal, 1));
        List<Meal> mealsWithUserID2 = Arrays.asList(
                new Meal(LocalDateTime.of(2018, Month.APRIL, 15, 8, 0), "Завтрак", 510),
                new Meal(LocalDateTime.of(2018, Month.APRIL, 15, 12, 0), "Обед", 510),
                new Meal(LocalDateTime.of(2018, Month.APRIL, 15, 20, 0), "Ужин", 510)
        );
        mealsWithUserID2.forEach(meal -> mealRepository.save(meal, 2));
    }

    @Test
    void save() {
        //New case
        int countBefore = mealRepository.getAll(1).size();
        Meal meal1 = new Meal(LocalDateTime.of(2017, Month.JUNE, 15, 20, 0), "Ужин", 510);
        Meal meal1Saved = mealRepository.save(meal1, 1);
        int countAfter = mealRepository.getAll(1).size();
        assertNotNull(meal1Saved);
        assertEquals(countBefore + 1, countAfter);

        //change parameters
        Meal meal1Changed = new Meal(meal1Saved.getId(), LocalDateTime.of(2017, Month.JUNE, 15, 20, 0), "Ужин-Changed", 510);

        Meal meal1ChangedSaved = mealRepository.save(meal1Changed, 1);
        assertNotNull(meal1ChangedSaved);

        Meal meal1ChangedSavedExtracted = mealRepository.get(meal1Saved.getId(), SecurityUtil.authUserId());
        assertEquals("Ужин-Changed", meal1ChangedSavedExtracted.getDescription());

        //try to save with not registered
        Meal mealNotExistingID = new Meal(1000, LocalDateTime.of(2017, Month.JUNE, 15, 20, 0), "Ужин-Changed", 510);
        assertNull(mealRepository.save(mealNotExistingID, 1));
        assertEquals(countAfter, mealRepository.getAll(1).size());

        //try to save when owner and users are different
        Meal existingMealDifferentUserID = new Meal(2, LocalDateTime.of(2017, Month.JUNE, 15, 20, 0), "All-Changed", 510);
        assertNull(mealRepository.save(existingMealDifferentUserID, 2));
        assertEquals(countAfter, mealRepository.getAll(1).size());


    }

    @Test
    void delete() {
        assertTrue(mealRepository.delete(6, 2));

        assertTrue(mealRepository.delete(2, SecurityUtil.authUserId()));
    }

    @Test
    void get() {
        assertNotNull(mealRepository.get(2, SecurityUtil.authUserId()));
        assertNull(mealRepository.get(7, SecurityUtil.authUserId()));
    }

    @Test
    void getAll() {
        //check list for registered users

        List<String> datesResultNullAndUser = Arrays.asList(
                LocalDateTime.of(2019, Month.MAY, 31, 20, 0).toString(),
                LocalDateTime.of(2019, Month.MAY, 31, 13, 0).toString(),
                LocalDateTime.of(2019, Month.MAY, 31, 9, 0).toString()
        );
        List<String> getAllResultNullAndUser = mealRepository.getAll(1).stream().map(meal -> meal.getDateTime().toString()).collect(Collectors.toList());

        assertLinesMatch(datesResultNullAndUser, getAllResultNullAndUser);

    }
}