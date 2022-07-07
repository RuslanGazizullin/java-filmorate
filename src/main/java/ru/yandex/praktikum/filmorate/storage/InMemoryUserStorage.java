package ru.yandex.praktikum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.validation.UserValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Autowired
    private UserValidation userValidation;

    private Long generateId() {
        return id++;
    }

    public User add(User user) throws ValidationException {
        userValidation.validateUser(user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Long id = generateId();
        user.setId(id);
        users.put(id, user);
        log.info("Пользователь успешно добавлен");
        return user;
    }

    public User update(User user) throws ValidationException {
        userValidation.idValidation(users, user);
        users.put(user.getId(), user);
        log.info("Пользователь успешно обновлён");
        return user;
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public List<User> findAll() {
        log.info("Список пользователей получен");
        return new ArrayList<>(users.values());
    }

    public User findById(Long id) {
        log.info("Пользователь найден");
        return users.get(id);
    }
}
