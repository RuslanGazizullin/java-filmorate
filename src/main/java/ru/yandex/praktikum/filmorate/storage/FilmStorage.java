package ru.yandex.praktikum.filmorate.storage;

import ru.yandex.praktikum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {

    Film add(Film film) throws ValidationException;

    Film update(Film film) throws ValidationException, ObjectNotFoundException;

    HashMap<Long, Film> getFilms();

    List<Film> findAll();

    Film findById(Long id) throws ObjectNotFoundException;
}
