package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealStorage implements Storage {
    private final AtomicInteger idAtomicCounter = new AtomicInteger(0);
    private Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Meal meal) {
        int id = idAtomicCounter.incrementAndGet();
        storage.put(id, meal);
        meal.setId(id);
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public List<Meal> getMeals() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}