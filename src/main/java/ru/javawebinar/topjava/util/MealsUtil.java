package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MealsUtil {
    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак1", 500),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед1", 1000),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин1", 500),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак1", 1000),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед1", 500),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин1", 510),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак2", 500),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед2", 1000),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин2", 500),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак2", 1000),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед2", 500),
            new Meal(null, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин2", 510)
    );

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealWithExceed> getWithExceeded(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExceeded(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return getFilteredWithExceeded(meals, caloriesPerDay, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }

    private static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

    public static MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }
}