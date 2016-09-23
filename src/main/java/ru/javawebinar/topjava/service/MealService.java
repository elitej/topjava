package ru.javawebinar.topjava.service;


import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;


public interface MealService {

    Meal save(Meal meal, int userId);

    void delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

    List<MealWithExceed> getAll(int userId);

    void update(Meal meal, int userId);

    List<MealWithExceed> getBetween(String dateFrom, String dateTo, String timeFrom, String timeTo, int userId);
}
