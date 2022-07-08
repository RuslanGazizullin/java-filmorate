package ru.yandex.praktikum.filmorate.validation;

import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;

@Component
public class UserValidation {

    public void emailValidation(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта отсутствует или неверный формат");
        }
    }

    public void loginValidation(User user) throws ValidationException {
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }
    }

    public void birthdayValidation(User user) throws ValidationException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неправильная дата рождения");
        }
    }

    public void idValidation(HashMap<Long, User> users, User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким id не существует");
        }
    }

    public void validateUser(User user) throws ValidationException {
        emailValidation(user);
        loginValidation(user);
        birthdayValidation(user);
    }
}
