package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<Integer, User> userRepository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public boolean delete(int id) {
        return userRepository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);

        if (user.isNew()) {
            user.setId(counter.getAndIncrement());
            userRepository.put(user.getId(), user);
            return user;
        }
        return userRepository.computeIfPresent(user.getId(), (key, oldValue) -> user);
    }

    @Override
    public User get(int id) {
        log.info("Get {}", id);
        return userRepository.get(id);
    }

    //return UserList sorted alphabetically by Name
    @Override
    public List<User> getAll() {
        log.info("getAll");
        return userRepository.values().stream()
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }


    //return the first User with the specified email, if many sorted by name
    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return userRepository.values().stream()
                .filter(x -> x.getEmail().equals(email))
                .min(Comparator.comparing(User::getName).thenComparing(User::getId)) //equivalent to sorted().getFirst
                .get();
    }
}