package ru.yandex.praktikum.filmorate.storage;

import ru.yandex.praktikum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User add(User user) throws ValidationException;

    User update(User user) throws ValidationException, ObjectNotFoundException;

    Map<Long, User> getUsers();

    List<User> findAll();

    User findById(Long id) throws ObjectNotFoundException;
}
