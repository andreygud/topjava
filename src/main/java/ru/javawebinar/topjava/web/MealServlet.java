package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private MealController mealController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        MealRepository repository = new InMemoryMealRepository();
        MealsUtil.MEALS.forEach(repository::save);
        mealController = new MealController(new MealService(repository));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Integer normalizedId = id.isEmpty() ? null : Integer.valueOf(id);

        MealTo mealTo = new MealTo(
                normalizedId,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")), false
        );

        if (normalizedId == null) {
            mealController.create(mealTo);
        } else {
            mealController.update(mealTo);
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final MealTo mealTo = "create".equals(action) ?
                        mealController.getDefaultMealTo() : mealController.get(getId(request));
                request.setAttribute("mealTo", mealTo);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", mealController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
