package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.storage.MealStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExceededInOnePass;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static MealStorage mealStorage = new MealStorage();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        List<MealWithExceed> mealWithExceedList = getFilteredWithExceededInOnePass(mealStorage.getMeals(),
                LocalTime.of(1, 0), LocalTime.of(23, 0), 2000);
        request.setAttribute("meals", mealWithExceedList);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}