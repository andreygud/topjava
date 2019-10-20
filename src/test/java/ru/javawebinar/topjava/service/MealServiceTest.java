package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealsTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealsTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/MealTestData.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void get_mealByOwner_positive() {
        Meal mealRetrieved = mealService.get(MEAL_OF_USER.getId(), USER_ID);
        assertThat(mealRetrieved).isEqualToIgnoringGivenFields(MEAL_OF_USER, "id");
    }

    public void get_mealByOther_negative() {
        Meal mealRetrieved = mealService.get(MEAL_OF_USER.getId(), ADMIN_ID);
        assertNull(mealRetrieved);
    }

    @Test
    public void delete_byOwner_success() {
        int before = mealService.getAll(USER_ID).size();
        mealService.delete(MEAL_OF_USER.getId(), USER_ID);
        int after = mealService.getAll(USER_ID).size();

        assertEquals(before - 1, after);
    }

    @Test(expected = NotFoundException.class)
    public void delete_byOther_fail() {
        mealService.delete(MEAL_OF_USER.getId(), ADMIN_ID);
    }

    @Test
    public void update_byOwner_success() {
        Meal meal = new Meal(MEAL_OF_USER);
        meal.setDateTime(LocalDateTime.parse("2019-10-20T22:00"));
        meal.setCalories(2000);
        meal.setDescription("After Lunch");

        mealService.update(meal, USER_ID);
        Meal updated = mealService.get(MEAL_OF_USER.getId(), USER_ID);

        assertThat(updated).isEqualTo(meal);
    }

    @Test(expected = NotFoundException.class)
    public void update_byOther_fail() {
        Meal meal = new Meal(MEAL_OF_USER);
        meal.setDateTime(LocalDateTime.parse("2019-10-20T22:00"));
        meal.setCalories(2000);
        meal.setDescription("After Lunch");

        mealService.update(MEAL_OF_USER, ADMIN_ID);
    }

    @Test
    public void create_newMeal_success() {
        Meal meal = new Meal(MEAL_OF_USER);
        meal.setId(null);
        meal.setDateTime(LocalDateTime.parse("2019-10-10T10:12"));

        int before = mealService.getAll(USER_ID).size();
        Meal createdMeal = mealService.create(meal, USER_ID);
        int after = mealService.getAll(USER_ID).size();

        assertEquals(before + 1, after);
        assertNotNull(createdMeal.getId());
        assertThat(createdMeal).isEqualToIgnoringGivenFields(meal, "id");
    }

    @Test(expected = DuplicateKeyException.class)
    public void create_newMealSameDate_fail() {
        Meal meal = new Meal(MEAL_OF_USER);
        meal.setId(null);
        mealService.create(meal, USER_ID);
    }

    @Test
    public void create_newMealSameDateOtherUser_success() {
        Meal meal = new Meal(MEAL_OF_USER);
        meal.setId(null);
        int before = mealService.getAll(ADMIN_ID).size();
        mealService.create(meal, ADMIN_ID);
        int after = mealService.getAll(ADMIN_ID).size();
        assertEquals(before + 1, after);
    }

    @Test
    public void getBetweenDates_byOwner_sortedList() {
       List<Meal> meals = mealService.getBetweenDates(LocalDate.parse("2015-05-30"),LocalDate.parse("2015-05-30"),USER_ID);
       Assertions.assertThat(meals).usingElementComparatorIgnoringFields("id").isEqualTo(MEALS_OF_USER);
    }

    @Test
    public void getAll_byOwner_sortedList() {
        List<Meal> meals = mealService.getAll(USER_ID);
        Assertions.assertThat(meals).usingElementComparatorIgnoringFields("id").isEqualTo(MEALS_OF_USER);
    }
}