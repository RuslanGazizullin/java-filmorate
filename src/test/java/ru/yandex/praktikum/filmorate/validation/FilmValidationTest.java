package ru.yandex.praktikum.filmorate.validation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {
    @Autowired
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
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.nameValidation(filmFailName)
        );
        assertEquals(validationException.getMessage(), "Отстутствует название",
                "Текст ошибки не совпадает");
    }

    @Test
    void testDescriptionValidation() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.descriptionValidation(filmFailDescription)
        );
        assertEquals(validationException.getMessage(), "Описание не должно превышать 200 символов",
                "Текст ошибки не совпадает");
    }

    @Test
    void testReleaseDateValidation() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.releaseDateValidation(filmFailReleaseDate)
        );
        assertEquals(validationException.getMessage(), "Дата релиза не должна быть раньше 28 декабря 1895г",
                "Текст ошибки не совпадает");
    }

    @Test
    void testDurationValidation() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.durationValidation(filmFailDuration)
        );
        assertEquals(validationException.getMessage(), "Продолжительность не может быть отрицательная",
                "Текст ошибки не совпадает");
    }

    @Test
    void idValidation() {
        films.put(1L, film);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmValidation.idValidation(films, updatedFilm)
        );
        assertEquals(validationException.getMessage(), "Фильм с таким id не существует",
                "Текст ошибки не совпадает");
    }
}