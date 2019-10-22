package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
    public void get_mealByOwner_success() {
        Meal mealRetrieved = mealService.get(MEAL_OF_USER.getId(), USER_ID);
        checkMealsAreEqual(mealRetrieved, MEAL_OF_USER);
    }

    @Test(expected = NotFoundException.class)
    public void get_mealByOther_fail() {
        mealService.get(MEAL_OF_USER.getId(), ADMIN_ID);
    }

    @Test
    public void delete_byOwner_success() {
        mealService.delete(MEAL_OF_USER.getId(), USER_ID);
        List<Meal> afterDelete = mealService.getAll(USER_ID);
        checkMealListsMatch(afterDelete, MEALS_OF_USER_AFTER_DELETE);
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

        checkMealsAreEqual(updated, meal);
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
        Meal meal = new Meal(MEAL_OF_USER_CREATED);
        meal.setId(null);

        Meal createdMeal = mealService.create(meal, USER_ID);
        List<Meal> afterCreate = mealService.getAll(USER_ID);

        checkMealsAreEqual(createdMeal, MEAL_OF_USER_CREATED);
        checkMealListsMatch(afterCreate, MEALS_OF_USER_AFTER_CREATE);
    }

    @Test(expected = DuplicateKeyException.class)
    public void create_newMealSameDateSameUser_fail() {
        Meal meal = new Meal(MEAL_OF_USER);
        meal.setId(null);
        mealService.create(meal, USER_ID);
    }

    @Test
    public void create_newMealSameDateOtherUser_success() {
        Meal meal = new Meal(MEAL_OF_USER);
        meal.setId(null);

        mealService.create(meal, ADMIN_ID);
        List<Meal> afterCreate = mealService.getAll(ADMIN_ID);

        checkMealListsMatch(afterCreate, MEALS_OF_ADMIN_AFTER_CREATE);
    }

    @Test
    public void getAll_byOwner_sortedList() {
        List<Meal> meals = mealService.getAll(USER_ID);
        checkMealListsMatch(meals, MEALS_OF_USER_ALL);
    }

    @Test
    public void getAll_byOther_sortedList() {
        List<Meal> meals = mealService.getAll(ADMIN_ID);
        checkMealListsMatch(meals, MEALS_OF_ADMIN_ALL);
    }

    @Test
    public void getBetween_byOwnerByDates_sortedList() {
        List<Meal> meals = mealService.getBetweenDates(LocalDate.parse("2019-10-23"), LocalDate.parse("2019-10-24"), USER_ID);
        checkMealListsMatch(meals, MEALS_OF_USER_FOR_DATES);
    }

    @Test
    public void getBetween_byOwnerByDate_onerecord() {
        List<Meal> meals = mealService.getBetweenDates(LocalDate.parse("2019-10-24"), LocalDate.parse("2019-10-24"), USER_ID);
        checkMealListsMatch(meals, Arrays.asList(MEAL_OF_USER5));
    }

    @Test
    public void getBetween_byOwnerDatesNull_sortedList_all() {
        List<Meal> meals = mealService.getBetweenDates(null, null, USER_ID);
        checkMealListsMatch(meals, MEALS_OF_USER_ALL);
    }

    @Test
    public void getBetween_byOtherDatesNull_listsDontMatch() {
        List<Meal> meals = mealService.getBetweenDates(null, null, ADMIN_ID);
        checkMealListsMatch(meals, MEALS_OF_ADMIN_ALL);
    }
}