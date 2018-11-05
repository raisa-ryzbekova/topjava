package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile("datajpa")
public class DataJpaMealRepositoryImpl implements MealRepository {

    private final CrudMealRepository crudMealRepository;

    private final CrudUserRepository crudUserRepository;

    @Autowired
    public DataJpaMealRepositoryImpl(CrudMealRepository mealRepository, CrudUserRepository userRepository) {
        this.crudMealRepository = mealRepository;
        this.crudUserRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(crudUserRepository.getOne(userId));
        return crudMealRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = this.get(id, userId);
        if (meal != null) {
            return crudMealRepository.delete(id) != 0;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudMealRepository.findById(id).orElse(null);
        if (meal != null && meal.getUser().getId() == userId) {
            return meal;
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudMealRepository.findByUserIdAndDateTimeBetweenOrderByDateTimeDesc(userId, startDate, endDate);
    }
}