package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private static final Comparator<Meal> DATE_TIME_COMPARATOR = Comparator.comparing(Meal::getDateTime);

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        Map<Integer, Meal> mealRepository = new HashMap<>();
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> mealMap = repository.get(userId);
            if (mealMap != null) {
                mealRepository = mealMap;
                mealRepository.put(meal.getId(), meal);
            } else {
                mealRepository.put(meal.getId(), meal);
                repository.put(userId, mealRepository);
            }
            return meal;
        }
        // treat case: update, but absent in storage
        return mealRepository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int mealId, int userId) {
        log.info("delete {}", mealId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null && userMeals.remove(mealId) != null;
    }

    @Override
    public Meal get(int mealId, int userId) {
        log.info("get {}", mealId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? null : userMeals.get(mealId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.get(userId).values().stream()
                .sorted(Collections.reverseOrder(DATE_TIME_COMPARATOR))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllFilteredDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll");
        return getAll(userId).stream()
                .filter(meal -> isBetween(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}
