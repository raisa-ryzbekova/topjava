package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private ProfileRestController profileRestController = new ProfileRestController();

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int mealId, int userId) {
        log.info("delete {}", mealId);
        if (repository.get(mealId).getUserId() == userId) {
            return repository.remove(mealId) != null;
        }
        return false;
    }

    @Override
    public Meal get(int mealId, int userId) {
        log.info("get {}", mealId);
        if (repository.get(mealId).getUserId() == userId) {
            return repository.get(mealId);
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Collections.reverseOrder(Meal.DATE_TIME_COMPARATOR))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAll() {
        log.info("getAll");
        return repository.values().stream()
                .sorted(Collections.reverseOrder(Meal.DATE_TIME_COMPARATOR))
                .collect(Collectors.toList());
    }
}
