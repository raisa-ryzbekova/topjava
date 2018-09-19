package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import static ru.javawebinar.topjava.util.TimeUtil.isBetween;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        List<UserMealWithExceed> userMealWithExceedList = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        System.out.println(userMealWithExceedList);
        //.toLocalDate();
        //.toLocalTime();
    }

    /*private static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateAndCalories = new LinkedHashMap<>();
        for (UserMeal userMeal : mealList) {
            dateAndCalories.merge(userMeal.getLocalDate(), userMeal.getCaloriesPerDay(), Integer::sum);
        }
        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            if (isBetween(userMeal.getLocalTime(), startTime, endTime)) {
                userMealWithExceedList.add(new UserMealWithExceed(userMeal, isExceeded(userMeal.getCaloriesPerDay(), caloriesPerDay)));
            }
        }
        return userMealWithExceedList;
    }*/

    private static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> datesAndCalories = mealList.stream()
                .collect(groupingBy(UserMeal::getLocalDate, reducing(0, UserMeal::getCalories, Integer::sum)));
        return mealList.stream()
                .filter(element -> isBetween(element.getLocalTime(), startTime, endTime))
                .map(element -> new UserMealWithExceed(element, isExceeded(datesAndCalories.get(element.getLocalDate()), caloriesPerDay)))
                .collect(toList());
    }

    private static boolean isExceeded(int calories, int caloriesPerDay) {
        return calories > caloriesPerDay;
    }
}

