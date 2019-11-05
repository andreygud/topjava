package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJpaTest extends MealServiceTest {

    @Test
    public void getWithUser() {
        Meal meal = service.getWithUser(MealTestData.MEAL1_ID, UserTestData.USER_ID);
        User user = meal.getUser();

        MealTestData.assertMatch(meal, MealTestData.MEAL1);
        UserTestData.assertMatch(user, UserTestData.USER);
    }
}
