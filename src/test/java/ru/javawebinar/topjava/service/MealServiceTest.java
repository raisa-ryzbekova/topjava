package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2018, 1, 3, 8, 0),
                "Завтрак", 500);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatchMeals(service.getAll(USER_ID), newMeal, MEAL_4, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL_1_ID, USER_ID);
        assertMatchMeals(meal, MEAL_1);
    }

    @Test
    public void delete() {
        service.delete(MEAL_1_ID, USER_ID);
        assertMatchMeals(service.getAll(USER_ID), MEAL_4, MEAL_3, MEAL_2);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> allBetweenDates = service.getBetweenDates(LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 1, 1), USER_ID);
        assertMatchMeals(allBetweenDates, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> allBetweenDates = service.getBetweenDateTimes(LocalDateTime.of(2018, 1, 1, 8, 0),
                LocalDateTime.of(2018, 1, 1, 12, 0), USER_ID);
        assertMatchMeals(allBetweenDates, MEAL_2, MEAL_1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatchMeals(all, MEAL_4, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL_1);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(1500);
        service.update(updated, USER_ID);
        assertMatchMeals(service.get(MEAL_1_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() {
        service.delete(MEAL_1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(MEAL_1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal updated = new Meal(MEAL_1);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(1500);
        service.update(updated, ADMIN_ID);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createWithTheSameDateTime() {
        Meal newMeal = new Meal(LocalDateTime.of(2018, 1, 1, 8, 0),
                "Завтрак", 500);
        service.create(newMeal, USER_ID);
    }
}