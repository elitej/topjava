package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;


public interface MealRepository {
    Meal save(Meal Meal, int userId);

    boolean delete(int id, int userId);

    Meal get(int id, int userId);

    default List<Meal> getAll(int userId) {
        return null;
    }

    List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);
}
