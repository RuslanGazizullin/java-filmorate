package ru.yandex.praktikum.filmorate.validation;

import org.springframework.stereotype.Component;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

@Component
public class FilmValidation {
    Film film;

    public FilmValidation(Film film) {
        this.film = film;
    }

    public void nameValidation() throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Отстутствует название");
        }
    }

    public void descriptionValidation() throws ValidationException {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно превышать 200 символов");
        }
    }

    public void releaseDateValidation() throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895г");
        }
    }

    public void durationValidation() throws ValidationException {
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательная");
        }
    }

    public void idValidation(HashMap<Long, Film> films) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с таким id не существует");
        }
    }
}
