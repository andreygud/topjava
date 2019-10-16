package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService mealService;

    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public void create(Meal meal) {
        log.debug("Create a mealTo {}", meal);

        mealService.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.debug("Update a mealTo {}", meal);
        assureIdConsistent(meal, id);
        mealService.update(meal, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("Get MealTo {}", id);
        return mealService.get(id, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.debug("delete MealTo {}", id);
        mealService.delete(id, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        log.debug("getDefaultMealTo");
        return getTos(mealService.getAllByAuthUser(SecurityUtil.authUserId()), DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllByTimeBoundaries(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.debug("getAllByTimeBoundaries");
        Collection<Meal> meals = mealService.getAllByDate(SecurityUtil.authUserId(),
                elvisOp(startDate, LocalDate.MIN),
                elvisOp(endDate, LocalDate.MAX));
        return getFilteredTos(meals, SecurityUtil.authUserCaloriesPerDay(),
                elvisOp(startTime, LocalTime.MIN),
                elvisOp(endTime, LocalTime.MAX));
    }
}
