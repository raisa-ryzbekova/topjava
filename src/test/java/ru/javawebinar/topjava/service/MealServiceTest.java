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
        Meal created = service.create(newMeal, 100000);
        newMeal.setId(created.getId());
        assertMatchMeals(service.getAll(100000), newMeal, MEAL_4, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL_1_ID, 100000);
        assertMatchMeals(meal, MEAL_1);
    }

    @Test
    public void delete() {
        service.delete(MEAL_1_ID, 100000);
        assertMatchMeals(service.getAll(100000), MEAL_4, MEAL_3, MEAL_2);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> allBetweenDates = service.getBetweenDates(LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 1, 1), 100000);
        assertMatchMeals(allBetweenDates, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> allBetweenDates = service.getBetweenDateTimes(LocalDateTime.of(2018, 1, 1, 8, 0),
                LocalDateTime.of(2018, 1, 1, 12, 0), 100000);
        assertMatchMeals(allBetweenDates, MEAL_2, MEAL_1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(100000);
        assertMatchMeals(all, MEAL_4, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL_1);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(1500);
        service.update(updated, 100000);
        assertMatchMeals(service.get(MEAL_1_ID, 100000), updated);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() {
        service.delete(MEAL_1_ID, 100001);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(MEAL_1_ID, 100001);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal updated = new Meal(MEAL_1);
        updated.setDescription("UpdatedDescription");
        updated.setCalories(1500);
        service.update(updated, 100001);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createWithTheSameDateTime() {
        Meal newMeal = new Meal(LocalDateTime.of(2018, 1, 1, 8, 0),
                "Завтрак", 500);
        service.create(newMeal, 100000);
    }
}