package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class Main {
    public static void main(String[] args) {
//        System.out.format("Hello Topjava Enterprise!");
        MealsUtil.getFilteredWithExceeded(MockDataBase.getListOfMeals(), LocalTime.MIN, LocalTime.MAX, 2000).forEach(System.out::println);
        MockDataBase.getMeals().values().forEach(System.out::println);
    }
}
