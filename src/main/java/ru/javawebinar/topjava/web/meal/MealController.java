package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.security.auth.AuthPermission;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.MealToValidationUtils.*;

@Controller
public class MealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService mealService;

    public MealController(MealService mealService) {
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

    public void update(MealTo mealTo) {
        log.debug("Update a mealTo {}", mealTo);

        validateDateIsMandatory(mealTo);
        validateDescriptionIsMandatory(mealTo);
        validateCaloriesIsManadatory(mealTo);

        Meal meal = new Meal(mealTo.getId(), mealTo.getDateTime(),
                mealTo.getDescription(), mealTo.getCalories(), SecurityUtil.authUserId());

        mealService.update(meal);
    }

    public MealTo get(int id) {
        log.info("Get MealTo {}", id);

        Meal meal = mealService.get(id, SecurityUtil.authUserId());

        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), false);
    }

    public MealTo getDefaultMealTo() {
        log.debug("getDefaultMealTo");
        return new MealTo(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, false);
    }

    public List<MealTo> getAll() {
        log.debug("getDefaultMealTo");

        return MealsUtil.getTos(mealService.getAllByAuthUser(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public void delete(int id) {
        log.debug("delete MealTo {}", id);

        mealService.delete(id, SecurityUtil.authUserId());
    }
}
