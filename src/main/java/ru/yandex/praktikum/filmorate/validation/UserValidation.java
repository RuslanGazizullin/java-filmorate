package ru.yandex.praktikum.filmorate.validation;

import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;

public class UserValidation {
    User user;

    public UserValidation(User user) {
        this.user = user;
    }

    public void emailValidation() throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта отсутствует или неверный формат");
        }
    }

    public void loginValidation() throws ValidationException {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }
    }

    public void birthdayValidation() throws ValidationException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неправильная дата рождения");
        }
    }

    public void idValidation(HashMap<Integer, User> users) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким id не существует");
        }
    }
}
