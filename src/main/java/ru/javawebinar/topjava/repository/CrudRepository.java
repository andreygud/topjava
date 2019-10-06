package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.GenericObject;
import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.List;

public interface CrudRepository<T extends GenericObject> {
    List<T> findAll();

    T save(T object);

    T findByID(Long id);

    void deleteByID(Long id);
}
