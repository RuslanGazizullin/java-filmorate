package ru.yandex.praktikum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private final static Logger log = LoggerFactory.getLogger(Film.class);

    private int generateId() {
        return id++;
    }

    @PostMapping()
    public Film add(@RequestBody Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("Валидация не пройдена");
            throw new ValidationException("Отстутствует название");
        } else if (film.getDescription().length() > 200) {
            log.warn("Валидация не пройдена");
            throw new ValidationException("Описание не должно превышать 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Валидация не пройдена");
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895г");
        } else if (film.getDuration() < 0) {
            log.warn("Валидация не пройдена");
            throw new ValidationException("Продолжительность не может быть отрицательная");
        } else {
            film.setId(generateId());
            films.put(film.getId(), film);
            log.info("Фильм успешно добавлен");
            return film;
        }
    }

    @PutMapping()
    public Film update(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм успешно обновлен");
            return film;
        } else {
            throw new ValidationException("Фильм с таким id не существует");
        }
    }

    @GetMapping()
    public ArrayList<Film> findAll() {
        return new ArrayList<>(films.values());
    }
}
