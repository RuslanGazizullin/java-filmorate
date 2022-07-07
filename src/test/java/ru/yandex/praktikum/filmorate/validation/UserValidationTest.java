package ru.yandex.praktikum.filmorate.validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    @Autowired
    UserValidation userValidation;
    User user = new User("user@user.ru", "user", "User_name", LocalDate.of(1986, 2, 19));
    User updatedUser = new User("user@user.ru", "user", "Updated_user_name", LocalDate.of(1986, 2, 19));
    User userFailEmail = new User("user.user.ru", "user", "User_name", LocalDate.of(1986, 2, 19));
    User userEmptyEmail = new User("", "user", "User_name", LocalDate.of(1986, 2, 19));
    User userFailLogin = new User("user@user.ru", "user 1", "User_name", LocalDate.of(1986, 2, 19));
    User userEmptyLogin = new User("user@user.ru", "", "User_name", LocalDate.of(1986, 2, 19));
    User userFailBirthday = new User("user@user.ru", "user", "User_name", LocalDate.of(2086, 2, 19));
    HashMap<Long, User> users = new HashMap<>();

    @Test
    void testEmptyEmailValidation() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.emailValidation(userEmptyEmail)
        );
        assertEquals(validationException.getMessage(), "Электронная почта отсутствует или неверный формат",
                "Текст ошибки не совпадает");
    }

    @Test
    void testFailEmailValidation() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.emailValidation(userFailEmail)
        );
        assertEquals(validationException.getMessage(), "Электронная почта отсутствует или неверный формат",
                "Текст ошибки не совпадает");
    }

    @Test
    void testEmptyLoginValidation() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.loginValidation(userEmptyLogin)
        );
        assertEquals(validationException.getMessage(), "Логин не может быть пустым или содержать пробелы",
                "Текст ошибки не совпадает");
    }

    @Test
    void testFailLoginValidation() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.loginValidation(userFailLogin)
        );
        assertEquals(validationException.getMessage(), "Логин не может быть пустым или содержать пробелы",
                "Текст ошибки не совпадает");
    }

    @Test
    void testFailBirthdayValidation() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.birthdayValidation(userFailBirthday)
        );
        assertEquals(validationException.getMessage(), "Неправильная дата рождения",
                "Текст ошибки не совпадает");
    }

    @Test
    void testIdValidation() {
        users.put(1L, user);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.idValidation(users, updatedUser)
        );
        assertEquals(validationException.getMessage(), "Пользователь с таким id не существует",
                "Текст ошибки не совпадает");
    }
}