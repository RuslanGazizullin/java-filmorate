package ru.yandex.praktikum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private int generateId() {
        return id++;
    }

    @PostMapping()
    public User add(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Валидация не пройдена");
            throw new ValidationException("Электронная почта отсутствует или неверный формат");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Валидация не пройдена");
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Валидация не пройдена");
            throw new ValidationException("Неправильная дата рождения");
        } else {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(generateId());
            users.put(user.getId(), user);
            log.info("Пользователь успешно добавлен");
            return user;
        }
    }

    @PutMapping()
    public User update(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь успешно обновлён");
            return user;
        } else {
            log.warn("Валидация не пройдена");
            throw new ValidationException("Фильм с таким id не существует");
        }
    }

    @GetMapping()
    public ArrayList<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
