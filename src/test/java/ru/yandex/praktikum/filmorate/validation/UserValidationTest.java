package ru.yandex.praktikum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

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
        userValidation = new UserValidation(userEmptyEmail);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.emailValidation()
        );
        assertEquals(validationException.getMessage(), "Электронная почта отсутствует или неверный формат",
                "Текст ошибки не совпадает");
    }

    @Test
    void testFailEmailValidation() {
        userValidation = new UserValidation(userFailEmail);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.emailValidation()
        );
        assertEquals(validationException.getMessage(), "Электронная почта отсутствует или неверный формат",
                "Текст ошибки не совпадает");
    }

    @Test
    void testEmptyLoginValidation() {
        userValidation = new UserValidation(userEmptyLogin);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.loginValidation()
        );
        assertEquals(validationException.getMessage(), "Логин не может быть пустым или содержать пробелы",
                "Текст ошибки не совпадает");
    }

    @Test
    void testFailLoginValidation() {
        userValidation = new UserValidation(userFailLogin);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.loginValidation()
        );
        assertEquals(validationException.getMessage(), "Логин не может быть пустым или содержать пробелы",
                "Текст ошибки не совпадает");
    }

    @Test
    void testFailBirthdayValidation() {
        userValidation = new UserValidation(userFailBirthday);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.birthdayValidation()
        );
        assertEquals(validationException.getMessage(), "Неправильная дата рождения",
                "Текст ошибки не совпадает");
    }

    @Test
    void testIdValidation() {
        users.put(1L, user);
        userValidation = new UserValidation(updatedUser);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userValidation.idValidation(users)
        );
        assertEquals(validationException.getMessage(), "Пользователь с таким id не существует",
                "Текст ошибки не совпадает");
    }
}