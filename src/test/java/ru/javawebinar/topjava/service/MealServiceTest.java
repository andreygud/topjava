package ru.javawebinar.topjava.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.*;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.sound.midi.Soundbank;
import java.time.LocalDate;
import java.time.Month;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);

    private static String watchedLog = "\n\nTest Execution Summary: \n";

    private TestWatcher watchman = new TestWatcher() {
        private long startTime;
        private long endTime;

        @Override
        protected void starting(Description description) {
            startTime = System.currentTimeMillis();
            super.starting(description);
        }

        @Override
        protected void failed(Throwable e, Description description) {
            endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            String output = "Execution time, ms: " + executionTime + " - " + description.getMethodName();

            watchedLog += output + "\n";
            log.debug(output, e);
        }

        @Override
        protected void succeeded(Description description) {
            endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            String output = "Execution time, ms: " + executionTime + " - " + description.getMethodName();

            watchedLog += output + "\n";
            log.debug(output);
        }
    };
    private TestName testName = new TestName();
    private ExpectedException exceptionChecker = ExpectedException.none();

    @Rule
    public final TestRule chain = RuleChain.outerRule(watchman).around(testName).around(exceptionChecker);

    @Autowired
    private MealService service;

    @AfterClass
    public static void printResults() {
        log.debug(watchedLog);
    }

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test //(expected = NotFoundException.class)
    public void deleteNotOwn() {
        exceptionChecker.expect(NotFoundException.class);
        exceptionChecker.reportMissingExceptionWithMessage(testName.getMethodName() + " method is expected to throw NotFoundException");
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test//(expected = NotFoundException.class)
    public void getNotFound() {
        exceptionChecker.expect(NotFoundException.class);
        exceptionChecker.reportMissingExceptionWithMessage(testName.getMethodName() + " method is expected to throw NotFoundException");
        service.get(1, USER_ID);
    }

    @Test//(expected = NotFoundException.class)
    public void getNotOwn() {
        exceptionChecker.expect(NotFoundException.class);
        exceptionChecker.reportMissingExceptionWithMessage(testName.getMethodName() + " method is expected to throw NotFoundException");
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test//(expected = NotFoundException.class)
    public void updateNotFound() {
        exceptionChecker.expect(NotFoundException.class);
        exceptionChecker.reportMissingExceptionWithMessage(testName.getMethodName() + " method is expected to throw NotFoundException");
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}