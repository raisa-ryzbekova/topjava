package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExceeded;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;
    private MealRepository repository = new InMemoryMealRepositoryImpl();

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal);
    }

    public Meal get(int mealId) {
        log.info("get {}", mealId);
        return service.get(mealId, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int mealId) {
        log.info("update {} with id={}", meal, mealId);
        assureIdConsistent(meal, mealId);
        service.update(meal);
    }

    public void delete(int mealId) {
        log.info("delete {}", mealId);
        service.delete(mealId, SecurityUtil.authUserId());
    }

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        List<MealWithExceed> mealWithExceeds = getWithExceeded(repository.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
        return mealWithExceeds;
    }

    public List<Meal> getAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getAll");
        List<Meal> meals = (List<Meal>) repository.getAll(SecurityUtil.authUserId());
        return meals.stream()
                .filter(meal -> isBetween(startDate, startTime, endDate, endTime, meal.getDate(), meal.getTime()))
                .collect(Collectors.toList());
    }

    public List<Meal> getAllMeals() {
        log.info("getAll");
        return (List<Meal>) repository.getAll(SecurityUtil.authUserId());
    }
}