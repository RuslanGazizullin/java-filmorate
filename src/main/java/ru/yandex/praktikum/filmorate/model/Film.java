package ru.yandex.praktikum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
}
