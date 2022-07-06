package ru.yandex.praktikum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.validation.UserValidation;

import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private Long id = 1L;

    private Long generateId() {
        return id++;
    }

    public User add(User user) throws ValidationException {
        UserValidation userValidation = new UserValidation(user);
        userValidation.emailValidation();
        userValidation.loginValidation();
        userValidation.birthdayValidation();
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Long id = generateId();
        user.setId(id);
        users.put(id, user);
        log.info("Пользователь успешно добавлен");
        return user;
    }

    public User update(User user) throws ValidationException, ObjectNotFoundException {
        if (users.containsKey(user.getId())) {
            UserValidation userValidation = new UserValidation(user);
            userValidation.idValidation(users);
            users.put(user.getId(), user);
            log.info("Пользователь успешно обновлён");
            return user;
        } else {
            log.warn("Пользователь с таким id не существует");
            throw new ObjectNotFoundException("Пользователь с таким id не существует");
        }
    }

    public HashMap<Long, User> getUsers() {
        return users;
    }

    public List<User> findAll() {
        log.info("Список пользователей получен");
        return users.values().stream().toList();
    }

    public User findById(Long id) throws ObjectNotFoundException {
        if (users.containsKey(id)) {
            log.info("Пользователь найден");
            return users.get(id);
        } else {
            log.warn("Пользователь с таким id не существует");
            throw new ObjectNotFoundException("Пользователь с таким id не существует");
        }
    }
}
