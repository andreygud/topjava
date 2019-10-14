package ru.javawebinar.topjava.repository.inmemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import javax.management.relation.RoleStatus;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private InMemoryUserRepository userRep;

    @BeforeEach
    void setUp() {
        userRep = new InMemoryUserRepository();

        //mix initiation order to test order in getAll()
        List<User> users = Arrays.asList(
                new User(null, "TestUser2", "test2@gmail.com", "pass", Role.ROLE_ADMIN),
                new User(null, "TestUser1", "test1@gmail.com", "pass", Role.ROLE_USER),
                new User(null, "TestUser4", "test4@gmail.com", "pass", Role.ROLE_USER),
                new User(null, "TestUser4", "test2@gmail.com", "pass", Role.ROLE_USER)
        );

        users.forEach(userRep::save);
    }

    @Test
    void delete() {

        userRep.delete(3);
        assertEquals(3, userRep.getAll().size());
    }

    @Test
    void save() {
        User user5 = new User(15, "TestUser4", "test4@gmail.com", "pass", Role.ROLE_USER);
        assertNull(userRep.save(user5));
        assertEquals(4, userRep.getAll().size());

        User user6 = new User(null, "myname", "some@email.com", "pass", Role.ROLE_USER);
        User user6Saved = userRep.save(user6);
        assertNotNull(user6Saved);
        Integer userId = user6Saved.getId();
        log.debug("Save - userId: {}", userId);
        assertNotNull(userId);

        User userByID = userRep.get(userId);
        assertEquals(userByID.getName(), user6.getName());
        assertEquals(userByID.getEmail(), user6.getEmail());
    }

    @Test
    void get() {
        User getUser = userRep.get(0);
        assertNotNull(getUser);
        assertEquals(0, getUser.getId());
    }

    @Test
    void getAll() {
        List<User> users = userRep.getAll();
        assertEquals(4, users.size());
        assertEquals("TestUser1", users.get(0).getName());
        assertEquals("TestUser2", users.get(1).getName());
        assertEquals("TestUser4-2", users.get(2).getName() + "-" + users.get(2).getId());
        assertEquals("TestUser4-3", users.get(3).getName() + "-" + users.get(3).getId());

    }

    @Test
    void getByEmail() {

        User user = userRep.getByEmail("test2@gmail.com");
        assertEquals("TestUser2", user.getName());

    }
}