package ru.javawebinar.topjava.service.jpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles(Profiles.JPA)
public class MealServiceJpaTest extends MealServiceTest {

    @Test
    public void getWithUser() {
        thrown.expect(UnsupportedOperationException.class);
        service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
    }
}
