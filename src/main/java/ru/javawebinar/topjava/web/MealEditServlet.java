package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.CrudRepository;
import ru.javawebinar.topjava.repository.MealInMemoryRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealEditServlet extends HttpServlet {
    private static final Logger log = getLogger(MealEditServlet.class);
    private CrudRepository<Meal> repositoryInstance = MealInMemoryRepository.getRepositoryInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        String id = req.getParameter("id");

        if ("edit".equals(action) && !"".equals(id) && (id != null)) {
            Meal meal = repositoryInstance.findByID(Long.decode(id));
            req.setAttribute("mealRecord", meal);
            req.getRequestDispatcher("/edit.jsp").forward(req, resp);
        } else if ("add".equals(action)) {
            req.getRequestDispatcher("/edit.jsp").forward(req, resp);
        } else if ("delete".equals(action) && !"".equals(id) && (id != null)) {
            repositoryInstance.deleteByID(Long.decode(id));
            resp.sendRedirect(req.getContextPath() + "/meals");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doPost IS executed");

        req.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");
        String description = req.getParameter("description");
        String datetime = req.getParameter("datetime");
        String calories = req.getParameter("calories");

        log.debug("String {} {} {} {}", id, description, datetime, calories);

        Meal mealRecord;

        if (!"".equals(id) && (id != null)) {
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
