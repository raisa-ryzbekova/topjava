package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.StringContains.containsString;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    private static Map<String, Long> testTimeData = new HashMap<>();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static void logInfo(Description description, long timeTest) {
        String testName = description.getMethodName();
        log.info(String.format("Test %s, spent %d ms",
                testName, TimeUnit.MILLISECONDS.toMillis(timeTest)));
    }

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long timeTest, Description description) {
            logInfo(description, timeTest);
            long timeMs = TimeUnit.MILLISECONDS.toMillis(timeTest) / 1000000;
            testTimeData.put(description.getMethodName(), timeMs);
        }
    };

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() {
        exception.expect(NotFoundException.class);
        exception.expectMessage(containsString("Not found entity with id=" + MEAL1_ID));
        service.delete(MEAL1_ID, 1);
    }

    @Test
    public void create() throws Exception {
        Meal created = getCreated();
        service.create(created, USER_ID);
        assertMatch(service.getAll(USER_ID), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() {
        exception.expect(NotFoundException.class);
        exception.expectMessage(containsString("Not found entity with id=" + MEAL1_ID));
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        exception.expect(NotFoundException.class);
        exception.expectMessage(containsString("Not found entity with id=" + MEAL1_ID));
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }

    @AfterClass
    public static void outTestTimeData() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("-------------------------------");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("    TEST            TIME(ms)");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("-------------------------------");
        stringBuilder.append(System.lineSeparator());
        for (String s : testTimeData.keySet()) {
            stringBuilder.append(String.format("%-20s %d", s, testTimeData.get(s)))
                    .append(System.lineSeparator());
        }
        stringBuilder.append("-------------------------------");
        stringBuilder.append(System.lineSeparator());
        log.info(stringBuilder.toString());
    }
}