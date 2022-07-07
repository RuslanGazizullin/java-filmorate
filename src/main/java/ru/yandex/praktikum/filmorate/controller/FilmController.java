package ru.yandex.praktikum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.Film;
import ru.yandex.praktikum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @PostMapping()
    public Film add(@RequestBody Film film) throws ValidationException {
        return filmService.add(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) throws ValidationException, ObjectNotFoundException {
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) throws ValidationException, ObjectNotFoundException {
        return filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) throws ValidationException, ObjectNotFoundException {
        return filmService.deleteLike(userId, id);
    }

    @GetMapping()
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Long id) throws ObjectNotFoundException {
        return filmService.findById(id);
    }

    @GetMapping("/popular")
    public List<Film> showMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.showMostPopularFilms(count);
    }
}
