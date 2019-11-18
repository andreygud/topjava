package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "meals")
public class JspMealController extends AbstractMealController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping(value = "")
    public String getAll(Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping(value = "/filter")
    public String filter(Model model, @RequestParam Map<String, String> allReqParams) {
        LocalDate startDate = parseLocalDate(allReqParams.get("startDate"));
        LocalDate endDate = parseLocalDate(allReqParams.get("endDate"));
        LocalTime startTime = parseLocalTime(allReqParams.get("startTime"));
        LocalTime endTime = parseLocalTime(allReqParams.get("endTime"));
        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping(value = "/createForm")
    public String createForm(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        model.addAttribute("isHeaderCreate", true);
        model.addAttribute("action","create");
        return "mealForm";
    }

    @GetMapping(value = "/updateForm/{id}")
    public String updateForm(Model model, @PathVariable int id) {
        final Meal meal = get(id);
        model.addAttribute("meal", meal);
        model.addAttribute("isHeaderCreate", false);
        model.addAttribute("action","update/"+id);
        return "mealForm";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteaction(@PathVariable int id) {
        delete(id);
        return "redirect:/meals";
    }

    @PostMapping("/create")
    public String createaction(String dateTime, String description, int calories) {
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        create(meal);
        return "redirect:/meals";
    }

    @PostMapping("/update/{id}")
    public String updateaction(String dateTime, String description, int calories,@PathVariable int id){
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        update(meal, id);
        return "redirect:/meals";
    }
}
