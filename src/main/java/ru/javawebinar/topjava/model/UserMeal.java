package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UserMeal {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private int caloriesPerDay;

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDate getLocalDate() {
        return this.dateTime.toLocalDate();
    }

    public LocalTime getLocalTime() {
        return this.dateTime.toLocalTime();
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }
}