package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static Map<Integer, User> userRepository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    {
        userRepository.put(1, new User(1, "Alex", "sss@mail.ru", "123", Role.ROLE_USER));
        userRepository.put(2, new User(2, "Valeriy", "embre@gmail.com", "228", Role.ROLE_ADMIN));
    }

    @Override
    public boolean delete(int id) {
        LOG.info("delete " + id);
        return userRepository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        LOG.info("save " + user);
        if (user.isNew())
        {
            user.setId(counter.incrementAndGet());
        }
        return userRepository.put(user.getId(), user);
    }

    @Override
    public User get(int id) {
        LOG.info("get " + id);
        return userRepository.get(id);
    }

    @Override
    public List<User> getAll() {
        LOG.info("getAll");

        List<User> result = new ArrayList<>(userRepository.values());
        Collections.sort(result, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        if (!result.isEmpty())
        {
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public User getByEmail(String email) {
        LOG.info("getByEmail " + email);
        return userRepository.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

}
