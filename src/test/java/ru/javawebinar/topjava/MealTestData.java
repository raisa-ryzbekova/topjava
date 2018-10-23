package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_1_ID = START_SEQ + 2;
    public static final int MEAL_2_ID = START_SEQ + 3;
    public static final int MEAL_3_ID = START_SEQ + 4;
    public static final int MEAL_4_ID = START_SEQ + 5;

    public static final Meal MEAL_1 = new Meal(MEAL_1_ID, LocalDateTime.of(2018, 1,
            1, 8, 0), "Завтрак0", 500);
    public static final Meal MEAL_2 = new Meal(MEAL_2_ID, LocalDateTime.of(2018, 1,
            1, 12, 0), "Обед0", 1000);
    public static final Meal MEAL_3 = new Meal(MEAL_3_ID, LocalDateTime.of(2018, 1,
            1, 17, 0), "Ужин0", 510);
    public static final Meal MEAL_4 = new Meal(MEAL_4_ID, LocalDateTime.of(2018, 1,
            2, 17, 0), "Завтрак0", 510);

    public static void assertMatchMeals(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatchMeals(Iterable<Meal> actual, Meal... expected) {
        assertMatchMeals(actual, Arrays.asList(expected));
    }

    private static void assertMatchMeals(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}