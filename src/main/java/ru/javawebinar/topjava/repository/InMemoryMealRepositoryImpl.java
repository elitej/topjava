package ru.javawebinar.topjava.repository;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
//        User testUser = new User(1, "Alex", "sss@mail.ru", "123", Role.ROLE_USER);
//        for (Meal meal : MealsUtil.MEALS)
//        {
//            save(meal, testUser.getId());
//        }
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        Map<Integer, Meal> innerMap = repository.get(meal.getUserId());
        innerMap.put(meal.getId(), meal);
        repository.put(meal.getUserId(), innerMap);
        return meal;
    }

    @Override
    public void delete(int id) {

        repository.remove(id);
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.get(userId).get(id);
    }

    @Override
    public List<Meal> getAll() {
        Collection<Meal> result = new TreeSet<>(((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime())));
        Collections.addAll(result,(Meal[]) repository.values().toArray());
        if (!result.isEmpty())
        {
            return (List<Meal>) result;
        }
        return Collections.emptyList();
    }
}

