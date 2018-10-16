package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {
    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(int mealId, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(mealId, userId), mealId);
    }

    @Override
    public Meal get(int mealId, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(mealId, userId), mealId);
    }

    @Override
    public List<Meal> getAll(int useId) {
        return (List<Meal>) repository.getAll();
    }

    @Override
    public void update(Meal meal) {
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }
}