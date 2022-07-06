package ru.yandex.praktikum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.validation.FilmValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private Long id = 1L;

    private Long generateId() {
        return id++;
    }

    public Film add(Film film) throws ValidationException {
        FilmValidation filmValidation = new FilmValidation(film);
        filmValidation.nameValidation();
        filmValidation.descriptionValidation();
        filmValidation.releaseDateValidation();
        filmValidation.durationValidation();
        Long id = generateId();
        film.setId(id);
        films.put(id, film);
        log.info("Фильм успешно добавлен");
        return film;
    }

    public Film update(Film film) throws ValidationException, ObjectNotFoundException {
        if (films.containsKey(film.getId())) {
            FilmValidation filmValidation = new FilmValidation(film);
            filmValidation.idValidation(films);
            films.put(film.getId(), film);
            log.info("Фильм успешно обновлен");
            return film;
        } else {
            log.warn("Фильм с таким id не существует");
            throw new ObjectNotFoundException("Фильм с таким id не существует");
        }
    }

    public HashMap<Long, Film> getFilms() {
        return films;
    }

    public List<Film> findAll() {
        log.info("Список фильмов получен");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(Long id) throws ObjectNotFoundException {
        if (films.containsKey(id)) {
            log.info("Фильм найден");
            return films.get(id);
        } else {
            log.warn("Фильм с таким id не существует");
            throw new ObjectNotFoundException("Фильм с таким id не существует");
        }
    }
}
