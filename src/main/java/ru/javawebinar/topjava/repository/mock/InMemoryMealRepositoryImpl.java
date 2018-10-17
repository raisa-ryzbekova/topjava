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

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenDateTime;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private Map<Integer, Meal> mealRepository;
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal.getUserId() == userId) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                if (repository.get(userId) != null) {
                    mealRepository = repository.get(userId);
                    mealRepository.put(meal.getId(), meal);
                } else {
                    mealRepository = new ConcurrentHashMap<>();
                    mealRepository.put(meal.getId(), meal);
                    repository.put(userId, mealRepository);
                }
                return meal;
            }
            // treat case: update, but absent in storage
            return mealRepository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return null;
    }

    @Override
    public boolean delete(int mealId, int userId) {
        log.info("delete {}", mealId);
        Meal meal = this.get(mealId, userId);
        return repository.get(userId).remove(meal.getId()) != null;
    }

    @Override
    public Meal get(int mealId, int userId) {
        log.info("get {}", mealId);
        Meal meal = repository.get(userId).get(mealId);
        if (meal.getUserId() == userId) {
            return meal;
        }
        return null;
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
        return repository.get(userId).values().stream()
                .filter(meal -> isBetweenDateTime(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }

    private final static Comparator<Meal> DATE_TIME_COMPARATOR = Comparator.comparing(Meal::getDateTime);
}
