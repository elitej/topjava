package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.ExceptionUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal save(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        ExceptionUtil.checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return ExceptionUtil.checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public List<MealWithExceed> getAll(int userId) {
        return MealsUtil.getWithExceeded(repository.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    @Override
    public void update(Meal meal, int userId) {
        repository.save(meal, userId);
    }

    @Override
    public List<MealWithExceed> getBetween(String dateFrom, String dateTo, String timeFrom, String timeTo, int userId) {
        LocalDate startDate = !dateFrom.equals("") ? LocalDate.parse(dateFrom) : LocalDate.MIN;
        LocalDate endDate = !dateTo.equals("") ? LocalDate.parse(dateTo) : LocalDate.MAX;
        LocalTime startTime = !timeFrom.equals("") ? LocalTime.parse(timeFrom) : LocalTime.MIN;
        LocalTime endTime = !timeTo.equals("") ? LocalTime.parse(timeTo) : LocalTime.MAX;

        return MealsUtil.getWithExceeded(repository.getBetween(LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime), userId),
                MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }


}
