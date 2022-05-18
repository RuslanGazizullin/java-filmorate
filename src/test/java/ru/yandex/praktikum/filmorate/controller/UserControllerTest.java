package ru.yandex.praktikum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController;

    User user = new User("user@user.ru", "user", "User_name", LocalDate.of(1986, 2, 19));
    User updatedUser = new User("user@user.ru", "user", "Updated_user_name", LocalDate.of(1986, 2, 19));
    User userFailEmail = new User("user.user.ru", "user", "User_name", LocalDate.of(1986, 2, 19));
    User userEmptyEmail = new User("", "user", "User_name", LocalDate.of(1986, 2, 19));
    User userFailLogin = new User("user@user.ru", "user 1", "User_name", LocalDate.of(1986, 2, 19));
    User userEmptyLogin = new User("user@user.ru", "", "User_name", LocalDate.of(1986, 2, 19));
    User userEmptyName = new User("user@user.ru", "user", "", LocalDate.of(1986, 2, 19));
    User userFailBirthday = new User("user@user.ru", "user", "User_name", LocalDate.of(2086, 2, 19));

    @BeforeEach
    void createController() {
        userController = new UserController();
    }

    @Test
    void testAdd() throws ValidationException {
        userController.add(user);
        assertEquals(userController.findAll().size(), 1, "Список пустой");
        assertEquals(userController.findAll().get(0), user, "Данные не совпадают");
    }

    @Test
    void testAddFailEmail() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userController.add(userFailEmail)
        );
        assertEquals(userController.findAll().size(), 0, "Список не пустой");
        assertEquals(validationException.getMessage(), "Электронная почта отсутствует или неверный формат",
                "Текст ошибки не совпадает");
    }

    @Test
    void testAddEmptyEmail() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userController.add(userEmptyEmail)
        );
        assertEquals(userController.findAll().size(), 0, "Список не пустой");
        assertEquals(validationException.getMessage(), "Электронная почта отсутствует или неверный формат",
                "Текст ошибки не совпадает");
    }

    @Test
    void testAddFailLogin() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userController.add(userFailLogin)
        );
        assertEquals(userController.findAll().size(), 0, "Список не пустой");
        assertEquals(validationException.getMessage(), "Логин не может быть пустым или содержать пробелы",
                "Текст ошибки не совпадает");
    }

    @Test
    void testAddEmptyLogin() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userController.add(userEmptyLogin)
        );
        assertEquals(userController.findAll().size(), 0, "Список не пустой");
        assertEquals(validationException.getMessage(), "Логин не может быть пустым или содержать пробелы",
                "Текст ошибки не совпадает");
    }

    @Test
    void testAddFailBirthday() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userController.add(userFailBirthday)
        );
        assertEquals(userController.findAll().size(), 0, "Список не пустой");
        assertEquals(validationException.getMessage(), "Неправильная дата рождения",
                "Текст ошибки не совпадает");
    }

    @Test
    void testAddEmptyName() throws ValidationException {
        userController.add(userEmptyName);
        userEmptyName.setName(userEmptyName.getLogin());
        assertEquals(userController.findAll().size(), 1, "Список пустой");
        assertEquals(userController.findAll().get(0), userEmptyName, "Данные не совпадают");
    }

    @Test
    void testUpdate() throws ValidationException {
        userController.add(user);
        updatedUser.setId(1);
        userController.update(updatedUser);
        assertEquals(userController.findAll().size(), 1, "Количество не совпадает");
        assertEquals(userController.findAll().get(0), updatedUser, "Данные не совпадают");
    }

    @Test
    void testUpdateFailId() throws ValidationException {
        userController.add(user);
        updatedUser.setId(-1);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> userController.update(updatedUser)
        );
        assertEquals(userController.findAll().size(), 1, "Количество не совпадает");
        assertEquals(userController.findAll().get(0), user, "Данные не совпадают");
        assertEquals(validationException.getMessage(), "Фильм с таким id не существует",
                "Текст ошибки не совпадает");
    }

    @Test
    void findAll() throws ValidationException {
        userController.add(user);
        assertEquals(userController.findAll().size(), 1, "Количество не совпадает");
        assertEquals(userController.findAll().get(0), user, "Данные не совпадают");
    }
}