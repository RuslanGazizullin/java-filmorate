package ru.yandex.praktikum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.validation.UserValidation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private int generateId() {
        return id++;
    }

    @PostMapping()
    public ResponseEntity<String> add(@RequestBody User user) {
        UserValidation userValidation = new UserValidation(user);
        try {
            userValidation.emailValidation();
            userValidation.loginValidation();
            userValidation.birthdayValidation();
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            int id = generateId();
            user.setId(id);
            users.put(id, user);
            log.info("Пользователь успешно добавлен");
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(user));
        } catch (ValidationException e) {
            log.warn("Валидация не пройдена");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<String> update(@RequestBody User user) {
        UserValidation userValidation = new UserValidation(user);
        try {
            userValidation.idValidation(users);
            users.put(user.getId(), user);
            log.info("Пользователь успешно обновлён");
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(user));
        } catch (ValidationException e) {
            log.warn("Валидация не пройдена");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping()
    public ArrayList<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
