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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private CrudRepository<Meal> repositoryInstance = MealInMemoryRepository.getRepositoryInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        String id = request.getParameter("id");

        log.debug("input parameters action {} id {}", action, id);


        switch (action) {
            case "edit":
                Meal meal = repositoryInstance.findByID(Long.decode(id));
                request.setAttribute("mealRecord", meal);
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
                break;
            case "add":
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
                break;
            case "delete":
                repositoryInstance.deleteByID(Long.decode(id));
                response.sendRedirect(request.getContextPath() + "/meals");
                break;
            default:
                List<Meal> meals = repositoryInstance.findAll();
                List<MealTo> mealToList = MealsUtil.getFiltered(meals, LocalTime.MIN, LocalTime.MAX, MealsUtil.DEFAULT_CALORIES_PER_DAY);
                request.setAttribute("mealtolist", mealToList);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doPost IS executed");

        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id") == null ? "" : req.getParameter("id");
        String description = req.getParameter("description");
        String datetime = req.getParameter("datetime");
        String calories = req.getParameter("calories");

        log.debug("String {} {} {} {}", id, description, datetime, calories);

        Meal mealRecord;
        if (!"".equals(id)) {
            mealRecord = repositoryInstance.findByID(Long.decode(id));
            if (mealRecord == null) {
                mealRecord = new Meal();
            }
        } else {
            mealRecord = new Meal();
        }

        mealRecord.setDateTime(LocalDateTime.parse(datetime));
        mealRecord.setCalories(Integer.decode(calories));
        mealRecord.setDescription(description);

        repositoryInstance.save(mealRecord);

        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
