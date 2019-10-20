package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealsTestData.MEALS_OF_USER;
import static ru.javawebinar.topjava.MealsTestData.MEAL_OF_USER;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/MealTestData.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest {

    @Autowired
    private JdbcMealRepository jdbcMealRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void save_new_positive() {

        int mealsCountBefore = jdbcMealRepository.getAll(USER_ID).size();

        Meal meal = new Meal(LocalDateTime.parse("2019-10-10T09:00"), "2й завтрак", 2000);
        Meal mealSaved = jdbcMealRepository.save(meal, USER_ID);

        int mealsCountAfter = jdbcMealRepository.getAll(USER_ID).size();
        assertEquals(mealsCountBefore + 1, mealsCountAfter);
        assertThat(mealSaved).isEqualToIgnoringGivenFields(meal, "id");
    }

    @Test
    public void save_existing_positive() {
        Meal meal = jdbcMealRepository.get(MEAL_OF_USER.getId(), USER_ID);
        int mealsCountBefore = jdbcMealRepository.getAll(USER_ID).size();

        meal.setDateTime(LocalDateTime.parse("2020-10-10T09:00"));
        meal.setDescription("something new");
        meal.setCalories(500);

        Meal mealSaved = jdbcMealRepository.save(meal, USER_ID);
        int mealsCountAfter = jdbcMealRepository.getAll(USER_ID).size();

        assertEquals(mealsCountBefore, mealsCountAfter);
        assertThat(mealSaved).isEqualTo(meal);
    }

    @Test
    public void save_existingOtherUser_negative() {
        Meal meal = jdbcMealRepository.get(MEAL_OF_USER.getId(), USER_ID);

        int mealsCountBefore = jdbcMealRepository.getAll(ADMIN_ID).size();
        Meal mealSaved = jdbcMealRepository.save(meal, ADMIN_ID);
        int mealsCountAfter = jdbcMealRepository.getAll(ADMIN_ID).size();

        assertNull(mealSaved);
        assertEquals(mealsCountBefore, mealsCountAfter);
    }

    @Test
    public void get_existingByOwner_positive() {
        Meal meal = jdbcMealRepository.get(MEAL_OF_USER.getId(), USER_ID);
        assertThat(meal).isEqualToIgnoringGivenFields(MEAL_OF_USER);
    }

    @Test
    public void get_existingByOtherUser_negative() {
        Meal meal = jdbcMealRepository.get(MEAL_OF_USER.getId(), ADMIN_ID);
        assertNull(meal);
    }

    @Test
    public void delete_existingByOwner_positive() {
        int mealsCountBefore = jdbcMealRepository.getAll(USER_ID).size();
        boolean result = jdbcMealRepository.delete(MEAL_OF_USER.getId(), USER_ID);
        int mealsCountAfter = jdbcMealRepository.getAll(USER_ID).size();

        assertTrue(result);
        assertEquals(mealsCountBefore - 1, mealsCountAfter);
    }

    @Test
    public void delete_existingByOtherUser_negative() {
        int mealsCountBefore = jdbcMealRepository.getAll(USER_ID).size();
        boolean result = jdbcMealRepository.delete(MEAL_OF_USER.getId(), ADMIN_ID);
        int mealsCountAfter = jdbcMealRepository.getAll(USER_ID).size();

        assertFalse(result);
        assertEquals(mealsCountBefore, mealsCountAfter);
    }

    @Test
    public void getBetween_getRecordsByOwnerForDates_onerecord() {
        List<Meal> meal = jdbcMealRepository.getBetween(LocalDateTime.parse("2015-05-30T10:00"), LocalDateTime.parse("2015-05-30T12:00"), USER_ID);
        assertThat(meal.get(0)).isEqualToIgnoringGivenFields(MEAL_OF_USER, "id");
    }

    @Test
    public void getBetween_getRecordsByOwnerNoFilter_sortedlist() {
        List<Meal> meal = jdbcMealRepository.getBetween(LocalDateTime.MIN, LocalDateTime.MAX, USER_ID);
        assertThat(meal).usingElementComparatorIgnoringFields("id").isEqualTo(MEALS_OF_USER);
    }

    @Test
    public void getBetween_getRecordsByOtherNoFilter_listsDontMatch() {
        List<Meal> meal = jdbcMealRepository.getBetween(LocalDateTime.MIN, LocalDateTime.MAX, ADMIN_ID);
        assertThat(meal).usingElementComparatorIgnoringFields("id").isNotEqualTo(MEALS_OF_USER);
    }

    @Test
    public void getAll_getRecordsByOwner_sortedlist() {
        List<Meal> meal = jdbcMealRepository.getAll(USER_ID);
        assertThat(meal).usingElementComparatorIgnoringFields("id").isEqualTo(MEALS_OF_USER);
    }

    @Test
    public void getAll_getRecordsByOther_listsDontMatch() {
        List<Meal> meal = jdbcMealRepository.getAll(ADMIN_ID);
        assertThat(meal).usingElementComparatorIgnoringFields("id").isNotEqualTo(MEALS_OF_USER);
    }
}