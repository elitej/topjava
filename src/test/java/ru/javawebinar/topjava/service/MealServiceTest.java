package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.BaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.jws.soap.SOAPBinding;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.model.BaseEntity.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() {
        dbPopulator.execute();
    }

    @Test
    public void testSave() {
        Meal meal = new Meal(null, LocalDateTime.now(), "Полдник", 332);
        Meal created = mealService.save(meal, USER_ID);
        Meal retrieved = mealService.get(MEAL_ID + 3, USER_ID);
        MATCHER.assertEquals(retrieved, created);
    }

    @Test
    public void testDelete() {
        mealService.delete(MEAL_ID, USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(DINNER, LUNCH), mealService.getAll(USER_ID));
    }

    @Test
    public void testGet() {
        Meal meal = mealService.get(MEAL_ID + 1, USER_ID);
        MATCHER.assertEquals(LUNCH, meal);
    }

    @Test(expected = NotFoundException.class)
    public void get_Should_ThrowNotFoundException_WhenPassedNotExistingMealIdInDB() {
        mealService.get(MEAL_ID + 3, USER_ID);
    }

    @Test
    public void testUpdate() {
        Meal meal = new Meal(null, LocalDateTime.now(), "abc", 322);
        mealService.save(meal, USER_ID);
        meal.setDescription("ddd");
        meal.setCalories(228);
        mealService.update(meal, USER_ID);
        MATCHER.assertEquals(mealService.get(MEAL_ID + 3, USER_ID), meal);
    }

    @Test
    public void testGetAll() {
        MATCHER.assertCollectionEquals(Arrays.asList(DINNER, LUNCH, BREAKFAST), mealService.getAll(USER_ID));
    }

    @Test
    public void testGetBetweenTimes() {
        LocalDateTime ldt1 = LocalDateTime.of(2017, 3, 27, 13, 57);
        LocalDateTime ldt2 = LocalDateTime.of(2017, 3, 27, 15, 12);
        Meal meal1 = new Meal(null, ldt1, "abc", 332);
        Meal meal2 = new Meal(null, ldt2, "def", 32);
        mealService.save(meal1, USER_ID);
        mealService.save(meal2, USER_ID);
        LocalDateTime end = LocalDateTime.of(2017, 3, 27, 14, 59);
        Collection<Meal> meals = mealService.getBetweenDateTimes(ldt1, end, USER_ID);
        MATCHER.assertCollectionEquals(Collections.singletonList(meal1), meals);
    }

    @Test
    public void testGetBetweenDates() {
        LocalDateTime ldt = LocalDateTime.of(2017, 3, 26, 20, 15);
        Meal save = mealService.save(new Meal(null, ldt, "abc", 228), USER_ID);
        Collection<Meal> meals = mealService.getBetweenDates(LocalDate.of(2017, 3, 25), LocalDate.of(2017, 3, 26), USER_ID);
        MATCHER.assertCollectionEquals(Collections.singletonList(save), meals);
    }

    @Test(expected = NotFoundException.class)
    public void get_ShouldThrowNotFoundException_WhenPassedAlienUserId() {
        Meal meal = mealService.get(MEAL_ID, USER_ID + 1);
    }

    @Test(expected = NotFoundException.class)
    public void delete_ShouldThrowNotFoundException_WhenPassedAlienUserId() {
        mealService.delete(MEAL_ID, USER_ID + 1);
    }

    @Test(expected = NotFoundException.class)
    public void update_ShouldThrowNotFoundException_WhenPassedAlienUserId() {
        Meal update = mealService.update(LUNCH, USER_ID + 1);
    }


}