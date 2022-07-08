package ru.yandex.praktikum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private UserStorage userStorage;

    public Film addLike(Long userId, Long filmId) throws ValidationException, ObjectNotFoundException {
        Film film = filmStorage.findById(filmId);
        if (film != null && userStorage.getUsers().containsKey(userId)) {
            film.getLikes().add(userId);
            filmStorage.update(film);
            log.info("Лайк успешно поставлен");
            return film;
        } else {
            throw new ObjectNotFoundException("Пользователь или фильм не найдены");
        }
    }

    public Film deleteLike(Long userId, Long filmId) throws ValidationException, ObjectNotFoundException {
        Film film = filmStorage.findById(filmId);
        if (film != null && userStorage.getUsers().containsKey(userId) &&
                film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
            filmStorage.update(film);
            log.info("Лайк успешно удален");
            return film;
        } else {
            throw new ObjectNotFoundException("Пользователь или фильм не найдены");
        }
    }

    public List<Film> showMostPopularFilms(Integer count) {
        ArrayList<Film> sortedFilms = new ArrayList<>(filmStorage.getFilms().values());
        sortedFilms.sort((film1, film2) -> film2.getLikes().size() - film1.getLikes().size());
        if (sortedFilms.size() == 0) {
            log.info("Фильмы не найдены");
            return sortedFilms;
        } else {
            log.info("Список самых популярных фильмов сформирован");
            return sortedFilms.stream().limit(count).collect(Collectors.toList());
        }
    }

    public Film add(Film film) throws ValidationException {
        return filmStorage.add(film);
    }

    public Film update(Film film) throws ValidationException, ObjectNotFoundException {
        if (filmStorage.getFilms().containsKey(film.getId())) {
            return filmStorage.update(film);
        } else {
            log.warn("Фильм с таким id не существует");
            throw new ObjectNotFoundException("Фильм с таким id не существует");
        }
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(Long id) throws ObjectNotFoundException {
        if (filmStorage.getFilms().containsKey(id)) {
            return filmStorage.findById(id);
        } else {
            log.warn("Фильм с таким id не существует");
            throw new ObjectNotFoundException("Фильм с таким id не существует");
        }
    }
}
