package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;


import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    {
        for (Meal meal : MealsUtil.MEALS)
        {
            save(meal, 1);
        }
        for (int i = 0; i < MealsUtil.MEALS.size(); i ++)
        {
            if (i % 2 != 0)
                save(MealsUtil.MEALS.get(i), 2);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        LOG.info("save");
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        Map<Integer, Meal> innerMap = repository.get(userId);
        if (innerMap != null)
        {
            innerMap.put(meal.getId(), meal);
            repository.put(userId, innerMap);
        }else
        {
            repository.put(userId, new ConcurrentHashMap<Integer, Meal>(){{put(meal.getId(), meal);}});
        }

        return meal;
    }

    @Override
    public boolean delete(int id, int userId)  {
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.get(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        LOG.info("getAll");
        List<Meal> result = new ArrayList<>(repository.get(userId).values());
        Collections.sort(result, (o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
        if (!result.isEmpty())
        {
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        List<Meal> result = getAll(userId).stream()
                .filter(d -> TimeUtil.isBetween(d.getDateTime(), startDateTime, endDateTime))
                .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                .collect(Collectors.toList());
        return result;
    }
}

