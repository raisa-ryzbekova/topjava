package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MapMealStorage implements Storage {
    private static final org.slf4j.Logger LOG = getLogger(MapMealStorage.class);
    private final AtomicInteger idAtomicCounter = new AtomicInteger(0);
    private Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public Meal save(Meal meal) {
        int id = idAtomicCounter.incrementAndGet();
        storage.put(id, meal);
        meal.setId(id);
        return meal;
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
        if (meal != null) {
            storage.put(meal.getId(), meal);
        } else {
            LOG.warn("Resume with uuid = " + meal.getId() + " doesn't exist");
            throw new NullPointerException();
        }
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}