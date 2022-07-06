package ru.yandex.praktikum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.storage.FilmStorage;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    @Autowired
    FilmStorage filmStorage;

    @Autowired
    UserStorage userStorage;

    static class TenMostPopularFilmComparator implements Comparator<Film> {
        @Override
        public int compare(Film film1, Film film2) {
            return film2.getLikes().size() - film1.getLikes().size();
        }
    }

    public Film addLike(Long userId, Long filmId) throws ValidationException, ObjectNotFoundException {
        Film film = filmStorage.getFilms().getOrDefault(filmId, null);
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
        Film film = filmStorage.getFilms().getOrDefault(filmId, null);
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
        TenMostPopularFilmComparator tenMostPopularFilmComparator = new TenMostPopularFilmComparator();
        ArrayList<Film> allFilms = new ArrayList<>(filmStorage.getFilms().values());
        allFilms.sort(tenMostPopularFilmComparator);
        if (allFilms.size() == 0) {
            log.info("Фильмы не найдены");
            return null;
        } else {
            log.info("Список самых популярных фильмов сформирован");
            return allFilms.stream().limit(count).collect(Collectors.toList());
        }
    }
}
