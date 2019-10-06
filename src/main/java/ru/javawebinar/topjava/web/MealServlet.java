package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.CrudRepository;
import ru.javawebinar.topjava.repository.MealInMemoryRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private CrudRepository<Meal> repositoryInstance = MealInMemoryRepository.getRepositoryInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to users");

        List<Meal> meals = repositoryInstance.findAll();

        List<MealTo> mealToList = MealsUtil.getFiltered(meals, LocalTime.MIN, LocalTime.MAX, MealsUtil.DEFAULT_CALORIES_PER_DAY);

        log.debug("mealToList {}", mealToList);

        request.setAttribute("mealtolist", mealToList);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
