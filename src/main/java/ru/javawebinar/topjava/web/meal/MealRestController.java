package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExceeded;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExceeded;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public Meal create(Meal meal, int userId) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public Meal get(int mealId) {
        log.info("get {}", mealId);
        return service.get(mealId, SecurityUtil.userId);
    }

    public void update(Meal meal, int userId) {
        log.info("update {} with id={}", meal, userId);
        assureIdConsistent(meal, userId);
        service.update(meal, userId);
    }

    public void delete(int mealId) {
        log.info("delete {}", mealId);
        service.delete(mealId, SecurityUtil.userId);
    }

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return getWithExceeded(service.getAll(SecurityUtil.userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getAllFilteredDateTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getAll");
        return getFilteredWithExceeded(service.getAllFilteredDate(SecurityUtil.userId, startDate, endDate),
                DEFAULT_CALORIES_PER_DAY, startTime, endTime);
    }
}