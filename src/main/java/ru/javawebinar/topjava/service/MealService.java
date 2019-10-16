package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealService {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal,int userId) {
        log.debug("Create a meal {}", meal);
        return repository.save(meal,userId );
    }

    public void update(Meal meal, int userID) throws NotFoundException {
        log.debug("Update a meal {}", meal);
        checkNotFoundWithId(repository.save(meal, userID), meal.getId());
    }

    public void delete(int id, int userId) throws NotFoundException {
        log.debug("Delete a meal {}", id);
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) throws NotFoundException {
        log.debug("Get a meal {}", id);
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAllByAuthUser(int userId) {
        log.debug("GetAll meals user={}", userId);
        return repository.getAll(userId);
    }

    public List<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.debug("getAllByTimeBoundaries user {} startDate {} endDate {}", userId, startDate, endDate);
        return repository.getAllByDate(userId, startDate, endDate);
    }
}