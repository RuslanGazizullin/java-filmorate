package ru.yandex.praktikum.filmorate.validation;

import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

@Component
public class FilmValidation {

    public void nameValidation(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Отстутствует название");
        }
    }

    public void descriptionValidation(Film film) throws ValidationException {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно превышать 200 символов");
        }
    }

    public void releaseDateValidation(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895г");
        }
    }

    public void durationValidation(Film film) throws ValidationException {
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательная");
        }
    }

    public void idValidation(HashMap<Long, Film> films, Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с таким id не существует");
        }
    }

    public void validateFilm(Film film) throws ValidationException {
        nameValidation(film);
        descriptionValidation(film);
        releaseDateValidation(film);
        durationValidation(film);
    }
}
