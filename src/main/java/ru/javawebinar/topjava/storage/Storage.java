package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    Meal save(Meal meal);

    Meal get(int id);

    List<Meal> getMeals();

    void update(Meal meal);

    void delete(int id);
}
