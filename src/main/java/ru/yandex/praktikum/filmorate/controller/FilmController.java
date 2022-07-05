package ru.yandex.praktikum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.validation.FilmValidation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private int generateId() {
        return id++;
    }

    @PostMapping()
    public ResponseEntity<String> add(@RequestBody Film film) {
        FilmValidation filmValidation = new FilmValidation(film);
        try {
            filmValidation.nameValidation();
            filmValidation.descriptionValidation();
            filmValidation.releaseDateValidation();
            filmValidation.durationValidation();
            int id = generateId();
            film.setId(id);
            films.put(id, film);
            log.info("Фильм успешно добавлен");
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(film));
        } catch (ValidationException e) {
            log.warn("Валидация не пройдена");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<String> update(@RequestBody Film film) {
        FilmValidation filmValidation = new FilmValidation(film);
        try {
            filmValidation.idValidation(films);
            films.put(film.getId(), film);
            log.info("Фильм успешно обновлен");
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(film));
        } catch (ValidationException e) {
            log.warn("Валидация не пройдена");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping()
    public ArrayList<Film> findAll() {
        return new ArrayList<>(films.values());
    }
}
