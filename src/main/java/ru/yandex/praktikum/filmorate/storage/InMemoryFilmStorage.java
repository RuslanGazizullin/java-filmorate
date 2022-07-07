package ru.yandex.praktikum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.validation.FilmValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private Long id = 1L;

    @Autowired
    private FilmValidation filmValidation;

    private Long generateId() {
        return id++;
    }

    public Film add(Film film) throws ValidationException {
        filmValidation.validateFilm(film);
        Long id = generateId();
        film.setId(id);
        films.put(id, film);
        log.info("Фильм успешно добавлен");
        return film;
    }

    public Film update(Film film) throws ValidationException {
        filmValidation.idValidation(films, film);
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен");
        return film;
    }

    public Map<Long, Film> getFilms() {
        return films;
    }

    public List<Film> findAll() {
        log.info("Список фильмов получен");
        return new ArrayList<>(films.values());
    }

    public Film findById(Long id) {
        log.info("Фильм найден");
        return films.get(id);
    }
}
