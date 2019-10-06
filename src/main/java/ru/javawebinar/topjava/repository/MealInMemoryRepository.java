package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.slf4j.LoggerFactory.getLogger;


public class MealInMemoryRepository implements CrudRepository<Meal> {

    private static final Logger log = getLogger(MealInMemoryRepository.class);

    private static MealInMemoryRepository repository = null;

    private ConcurrentMap<Long, Meal> mealsMap = new ConcurrentHashMap<>();

    private MealInMemoryRepository() {
        init();
        log.debug("MealRepository initialization finished: {} ",mealsMap);
    }

    synchronized public static MealInMemoryRepository getRepositoryInstance() {
        if (repository == null) {
            repository = new MealInMemoryRepository();
        }
        return repository;
    }

    public Meal save(Meal object) {
        if (object == null) throw new IllegalArgumentException("object cannot be null");

        if (object.getId() == null) {
            object.setId(getNextID());
        }
        mealsMap.put(object.getId(), object);

        return object;
    }

    @Override
    public Meal findByID(Long id) {
        return mealsMap.get(id);
    }

    @Override
    public void deleteByID(Long id) {
        mealsMap.remove(id);
    }


    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(mealsMap.values());
    }

    synchronized private Long getNextID() {
        Long nextId;
        try {
            nextId = Collections.max(mealsMap.keySet()) + 1L;
        } catch (NoSuchElementException e) {
            nextId = 1L;
        }
        return nextId;
    }


    private void init() {
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }
}
