package ru.yandex.praktikum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController;

    Film film = new Film("Film 1", "Film 1", LocalDate.of(2012, 12, 12), 190);
    Film filmFailName = new Film("", "Film 2", LocalDate.of(2012, 12, 12), 190);
    Film filmFailDescription = new Film("Film 3", "Пятеро друзей ( комик-группа «Шарло»)," +
            " приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
            " а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
            LocalDate.of(2012, 12, 12), 190);
    Film filmFailReleaseDate = new Film("Film 4", "Film 4", LocalDate.of(1812, 12, 12), 190);
    Film filmFailDuration = new Film("Film 5", "Film 5", LocalDate.of(2012, 12, 12), -190);
    Film updatedFilm = new Film("Film 1", "Updated Film 1", LocalDate.of(2012, 12, 12), 190);

    @BeforeEach
    void createController() {
        filmController = new FilmController();
    }

    @Test
    void testAdd() throws ValidationException {
        filmController.add(film);
        assertEquals(filmController.findAll().get(0), film, "Данные не совпадают");
    }

    @Test
    void testAddFailName() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmController.add(filmFailName)
        );
        assertEquals(filmController.findAll().size(), 0, "Список фильмов не пустой");
        assertEquals(validationException.getMessage(), "Отстутствует название", "Текст ошибки не совпадает");
    }

    @Test
    void testAddFailDescription() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmController.add(filmFailDescription)
        );
        assertEquals(filmController.findAll().size(), 0, "Список фильмов не пустой");
        assertEquals(validationException.getMessage(), "Описание не должно превышать 200 символов",
                "Текст ошибки не совпадает");
    }

    @Test
    void testAddFailReleaseDate() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmController.add(filmFailReleaseDate)
        );
        assertEquals(filmController.findAll().size(), 0, "Список фильмов не пустой");
        assertEquals(validationException.getMessage(), "Дата релиза не должна быть раньше 28 декабря 1895г",
                "Текст ошибки не совпадает");
    }

    @Test
    void testAddFailDuration() {
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmController.add(filmFailDuration)
        );
        assertEquals(filmController.findAll().size(), 0, "Список фильмов не пустой");
        assertEquals(validationException.getMessage(), "Продолжительность не может быть отрицательная",
                "Текст ошибки не совпадает");
    }

    @Test
    void testUpdate() throws ValidationException {
        filmController.add(film);
        updatedFilm.setId(1);
        filmController.update(updatedFilm);
        assertEquals(filmController.findAll().get(0), updatedFilm, "Данные не совпадают");
    }

    @Test
    void testUpdateFailId() throws ValidationException {
        filmController.add(film);
        updatedFilm.setId(-1);
        final ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> filmController.update(updatedFilm)
        );
        assertEquals(filmController.findAll().get(0), film, "Данные не совпадают");
        assertEquals(validationException.getMessage(), "Фильм с таким id не существует",
                "Текст ошибки не совпадает");
    }

    @Test
    void findAll() throws ValidationException {
        filmController.add(film);
        assertEquals(filmController.findAll().size(), 1, "Количество не совпадает");
        assertEquals(filmController.findAll().get(0), film, "Данные не совпадают");
    }
}