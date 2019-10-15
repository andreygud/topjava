package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.MealRestControllerUtils.*;
import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService mealService;

    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public void create(MealTo mealTo) {
        log.debug("Create a mealTo {}", mealTo);

        validateIsNew(mealTo);
        validateDateIsMandatory(mealTo);
        validateDescriptionIsMandatory(mealTo);
        validateCaloriesIsManadatory(mealTo);

        Meal meal = new Meal(mealTo.getDateTime(),
                mealTo.getDescription(), mealTo.getCalories(), SecurityUtil.authUserId());

        mealService.create(meal);
    }

    public void update(MealTo mealTo, int id) {
        log.debug("Update a mealTo {}", mealTo);

        validateDateIsMandatory(mealTo);
        validateDescriptionIsMandatory(mealTo);
        validateCaloriesIsManadatory(mealTo);


        Meal meal = new Meal(mealTo.getId(), mealTo.getDateTime(),
                mealTo.getDescription(), mealTo.getCalories(), SecurityUtil.authUserId());
        assureIdConsistent(meal, id);

        mealService.update(meal);
    }

    public MealTo get(int id) {
        log.info("Get MealTo {}", id);

        Meal meal = mealService.get(id, SecurityUtil.authUserId());

        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), false);
    }

    public void delete(int id) {
        log.debug("delete MealTo {}", id);

        mealService.delete(id, SecurityUtil.authUserId());
    }

    public MealTo getDefaultMealTo() {
        log.debug("getDefaultMealTo");
        return new MealTo(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, false);
    }

    public List<MealTo> getAll() {
        log.debug("getDefaultMealTo");

        return getTos(mealService.getAllByAuthUser(SecurityUtil.authUserId()), DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllByTimeBoundaries(String startDate, String endDate, String startTime, String endTime) {
        log.debug("getAllByTimeBoundaries");

        Map<LocalDate, Integer> caloriesSumByDate = mealService.getCaloriesSumByDate(SecurityUtil.authUserId());
        List<Meal> meals = mealService
                .getAllByTimeBoundaries(SecurityUtil.authUserId(),
                        parseDateBoundary(startDate, LocalDate.MIN),
                        parseDateBoundary(endDate, LocalDate.MAX),
                        parseTimeBoundary(startTime, LocalTime.MIN),
                        parseTimeBoundary(endTime, LocalTime.MAX)
                );

        return convertMealIntoMealTo(meals, caloriesSumByDate, SecurityUtil.authUserCaloriesPerDay());
    }

}
