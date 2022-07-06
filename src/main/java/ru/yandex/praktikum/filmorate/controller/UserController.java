package ru.yandex.praktikum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.service.UserService;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserStorage userStorage;

    @Autowired
    UserService userService;

    @PostMapping()
    public User add(@RequestBody User user) throws ValidationException {
        return userStorage.add(user);
    }

    @PutMapping()
    public User update(@RequestBody User user) throws ValidationException, ObjectNotFoundException {
        return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) throws ValidationException, ObjectNotFoundException {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id, @PathVariable Long friendId) throws ValidationException, ObjectNotFoundException {
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping()
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) throws ObjectNotFoundException {
        return userStorage.findById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable Long id) throws ObjectNotFoundException {
        return userService.findFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) throws ObjectNotFoundException {
        return userService.findCommonFriends(id, otherId);
    }
}
