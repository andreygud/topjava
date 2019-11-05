package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(UserTestData.USER_ID);
        List<Meal> meals = user.getMeals();

        UserTestData.assertMatch(user,UserTestData.USER);
        MealTestData.assertMatch(meals, MealTestData.MEALS);
    }

    @Test
    public void getWithMeals_noMeals_emptyList() {
        User user = service.getWithMeals(UserTestData.USER_EMPTYMEAL_ID);
        List<Meal> meals = user.getMeals();

        UserTestData.assertMatch(user,UserTestData.USER_EMPTYMEAL);
        assertEquals(meals.size(), 0);
    }
}