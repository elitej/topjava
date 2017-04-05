package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: gkisline
 * Date: 26.08.2014
 */

@Repository
@Transactional
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Meal save(Meal meal, int userId) {
        User user = em.getReference(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(user);
            em.persist(meal);
            return meal;
        } else {
            TypedQuery<Meal> query = em.createQuery("select m from Meal m " +
                    "where m.id =?1 " +
                    "and m.user.id=?2", Meal.class);
            query.setParameter(1, meal.getId());
            query.setParameter(2, userId);
            List<Meal> resultList = query.getResultList();
            if (resultList.size() == 0)
                return null;
            meal.setUser(user);
            return em.merge(meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = get(id, userId);
        if (meal != null) {
            em.remove(meal);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Query query = em.createQuery("SELECT m from Meal m where m.id=?1 and m.user.id=?2");
        query.setParameter(1, id);
        query.setParameter(2, userId);
        List resultList = query.getResultList();
        return (resultList.size() == 0) ? null : (Meal) resultList.get(0);
    }

    @Override
    public List<Meal> getAll(int userId) {
        TypedQuery<Meal> query = em.createNamedQuery("getAllMealForUser", Meal.class);
        query.setParameter(1, userId);
        return query.getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        TypedQuery<Meal> query = em.createQuery("select m from Meal m " +
                "WHERE m.user.id=?1 " +
                "and m.dateTime between ?2 and ?3" +
                " order by m.dateTime desc", Meal.class);
        query.setParameter(1, userId);
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);
        List<Meal> resultList = query.getResultList();
        return (resultList.size() == 0) ? null : resultList;

    }
}