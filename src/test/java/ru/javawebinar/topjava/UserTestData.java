package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;

import java.util.Collections;
import java.util.Date;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", 2005, Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", 1900, Role.ROLE_ADMIN, Role.ROLE_USER);

    public static final User UPDATE_USER_INVALID_NAME_EMAIL_PASS_CALORIES = new User(USER_ID, "n", "wrong@email", "1234", 9, Role.ROLE_USER);
    public static final User CREATE_USER_INVALID_NAME_EMAIL_PASS_CALORIES = new User(null, "n", "wrong@email", "1234", 9, Role.ROLE_USER);

    public static final UserTo UPDATE_USERTO_INVALID_NAME_EMAIL_PASS_CALORIES = new UserTo(USER_ID, "n", "wrong@email", "1234", 9);
    public static final UserTo CREATE_USERTO_INVALID_NAME_EMAIL_PASS_CALORIES = new UserTo(null, "n", "wrong@email", "1234", 9);


    public static final String VALIDATIONS_RESPONSE_BODY = "{\"type\":\"VALIDATION_ERROR\",\"details\":[\"[caloriesPerDay] must be between 10 and 10000\",\"[password] size must be between 5 and 100\",\"[name] size must be between 2 and 100\"]}";
    public static final String VALIDATIONS_RESPONSE_BODY_USER_TO = "{\"type\":\"VALIDATION_ERROR\",\"details\":[\"[password] length must be between 5 and 32 characters\",\"[caloriesPerDay] must be between 10 and 10000\",\"[name] size must be between 2 and 100\"]}";

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.ROLE_USER));
    }

    public static User getUpdated() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        return updated;
    }

    public static TestMatchers<User> USER_MATCHERS = TestMatchers.useFieldsComparator(User.class, "registered", "meals", "password");
}
