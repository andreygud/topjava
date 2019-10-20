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


@ContextConfiguration({"classpath:spring/spring-app.xml","classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest {

    @Autowired
    JdbcMealRepository jdbcMealRepository;

    public static final Meal MEAL = new Meal(LocalDateTime.parse("2015-05-30T10:00:00"),"Завтрак",500);


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void get() {
        Meal meal = jdbcMealRepository.get(100002,100000);
        assertThat(meal).isEqualToIgnoringGivenFields(MEAL,"id");

    }

    @Test
    public void getBetween() {
        List<Meal> meal = jdbcMealRepository.getBetween(LocalDateTime.parse("2015-05-30T10:00"),LocalDateTime.parse("2015-05-30T12:00"),100000);
        assertThat(meal.get(0)).isEqualToIgnoringGivenFields(MEAL,"id");
    }
}