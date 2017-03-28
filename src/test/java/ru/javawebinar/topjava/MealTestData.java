package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;


public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int MEAL_ID = START_SEQ + 2;

    public static final Meal BREAKFAST = new Meal(MEAL_ID, LocalDateTime.now(), "Завтрак", 500);
    public static final Meal LUNCH = new Meal(MEAL_ID + 1, LocalDateTime.now(), "Обед", 510);
    public static final Meal DINNER = new Meal(MEAL_ID + 2, LocalDateTime.now(), "Ужин", 500);

    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher<>((expected, actual) -> expected == actual ||
            Objects.equals(expected.getId(), actual.getId()) &&
            Objects.equals(expected.getDate(), actual.getDate()) &&
            Objects.equals(expected.getDescription(), actual.getDescription()) &&
            Objects.equals(expected.getCalories(), actual.getCalories())
    );
}
