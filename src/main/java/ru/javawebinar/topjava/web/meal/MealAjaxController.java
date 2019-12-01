package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/ajax/meals")
public class MealAjaxController extends AbstractMealController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll(){
        return super.getAll();
    }


    @DeleteMapping
    @RequestMapping("/{id}")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void create(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                       @RequestParam @Nullable String description,
                       @RequestParam @Nullable Integer calories) {

        Meal meal = new Meal(dateTime, description, calories);

        super.create(meal);
    }


}
