package ru.javawebinar.topjava;

import ru.javawebinar.topjava.util.MealsUtil;


public class AuthorizedUser {

    private static int id;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        AuthorizedUser.id = id;
    }




    public static int getCaloriesPerDay() {
        return MealsUtil.DEFAULT_CALORIES_PER_DAY;
    }
}
