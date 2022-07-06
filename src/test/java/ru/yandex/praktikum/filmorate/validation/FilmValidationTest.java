package ru.yandex.praktikum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {

    FilmValidation filmValidation;
    Film film = new Film("Film 1", "Film 1", LocalDate.of(2012, 12, 12), 190);
    Film updatedFilm = new Film("Film 1", "Updated Film 1", LocalDate.of(2012, 12, 12), 190);
    Film filmFailName = new Film("", "Film 2", LocalDate.of(2012, 12, 12), 190);
    Film filmFailDescription = new Film("Film 3", "Пятеро друзей ( комик-группа «Шарло»)," +
            " приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
            " а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
            LocalDate.of(2012, 12, 12), 190);
    Film filmFailReleaseDate = new Film("Film 4", "Film 4", LocalDate.of(1812, 12, 12), 190);
    Film filmFailDuration = new Film("Film 5", "Film 5", LocalDate.of(2012, 12, 12), -190);
    HashMap<Long, Film> films = new HashMap<>();

    @Test
    void testNameValidation() {
        filmValidation = new FilmValidation(filmFailName);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.nameValidation()
        );
        assertEquals(validationException.getMessage(), "Отстутствует название",
                "Текст ошибки не совпадает");
    }

    @Test
    void testDescriptionValidation() {
        filmValidation = new FilmValidation(filmFailDescription);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.descriptionValidation()
        );
        assertEquals(validationException.getMessage(), "Описание не должно превышать 200 символов",
                "Текст ошибки не совпадает");
    }

    @Test
    void testReleaseDateValidation() {
        filmValidation = new FilmValidation(filmFailReleaseDate);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.releaseDateValidation()
        );
        assertEquals(validationException.getMessage(), "Дата релиза не должна быть раньше 28 декабря 1895г",
                "Текст ошибки не совпадает");
    }

    @Test
    void testDurationValidation() {
        filmValidation = new FilmValidation(filmFailDuration);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.durationValidation()
        );
        assertEquals(validationException.getMessage(), "Продолжительность не может быть отрицательная",
                "Текст ошибки не совпадает");
    }

    @Test
    void idValidation() {
        films.put(1L, film);
        filmValidation = new FilmValidation(updatedFilm);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.idValidation(films)
        );
        assertEquals(validationException.getMessage(), "Фильм с таким id не существует",
                "Текст ошибки не совпадает");
    }
}