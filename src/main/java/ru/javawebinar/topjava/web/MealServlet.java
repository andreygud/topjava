package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);

        MealRepository mealRepository = appCtx.getBean(MealRepository.class);
        MealsUtil.MEALS.forEach(mealRepository::save);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Integer normalizedId = id.isEmpty() ? null : Integer.valueOf(id);

        Meal meal = new Meal(
                normalizedId,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))
        );

        if (normalizedId == null) {
            mealRestController.create(meal);
        } else {
            mealRestController.update(meal, normalizedId);
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
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        getDefaultMealFrom() : mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filtered":
                log.info("getfiltered");
                request.setAttribute("meals",
                        mealRestController.getAllByTimeBoundaries(
                                parseDateBoundary(request.getParameter("dateStart"), LocalDate.MIN),
                                parseDateBoundary(request.getParameter("dateEnd"), LocalDate.MAX),
                                parseTimeBoundary(request.getParameter("timeStart"), LocalTime.MIN),
                                parseTimeBoundary(request.getParameter("timeEnd"), LocalTime.MAX)
                        ));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private Meal getDefaultMealFrom() {
        log.debug("getDefaultMealTo");

        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, 0);
    }

    private static LocalTime parseTimeBoundary(String startTime, LocalTime boundary) {

        if (startTime == null || "".equals(startTime)) {
            return boundary;
        }
        return LocalTime.parse(startTime);
    }

    private static LocalDate parseDateBoundary(String startDate, LocalDate boundary) {

        if (startDate == null || "".equals(startDate)) {
            return boundary;
        }
        return LocalDate.parse(startDate);
    }


}
