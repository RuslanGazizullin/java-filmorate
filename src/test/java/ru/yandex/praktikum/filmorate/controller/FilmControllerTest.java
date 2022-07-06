package ru.yandex.praktikum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.praktikum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.praktikum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    MockMvc mockMvc;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    Film film = new Film("Film 1", "Film 1", LocalDate.of(2012, 12, 12), 190);
    Film filmFailName = new Film("", "Film 2", LocalDate.of(2012, 12, 12), 190);
    Film filmFailDescription = new Film("Film 3", "Пятеро друзей ( комик-группа «Шарло»)," +
            " приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
            " а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.",
            LocalDate.of(2012, 12, 12), 190);
    Film filmFailReleaseDate = new Film("Film 4", "Film 4", LocalDate.of(1812, 12, 12), 190);
    Film filmFailDuration = new Film("Film 5", "Film 5", LocalDate.of(2012, 12, 12), -190);
    Film updatedFilm = new Film("Film 1", "Updated Film 1", LocalDate.of(2012, 12, 12), 190);

    @Test
    @DirtiesContext
    void testFindAll() throws Exception {
        ArrayList<Film> films = new ArrayList<>();
        film.setId(1L);
        films.add(film);
        mockMvc.perform(post("/films")
                .content(gson.toJson(film))
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(get("/films"))
                .andExpect(result -> gson.toJson(films))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void testFindAllNoFilms() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(content().string("[]"))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void testAdd() throws Exception {
        mockMvc.perform(post("/films")
                        .content(gson.toJson(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> gson.toJson(film));
    }

    @Test
    @DirtiesContext
    void testAddFailName() throws Exception {
        mockMvc.perform(post("/films")
                        .content(gson.toJson(filmFailName))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Отстутствует название"));
    }

    @Test
    @DirtiesContext
    void testAddFailDescription() throws Exception {
        mockMvc.perform(post("/films")
                        .content(gson.toJson(filmFailDescription))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Описание не должно превышать 200 символов"));
    }

    @Test
    @DirtiesContext
    void testAddFailReleaseDate() throws Exception {
        mockMvc.perform(post("/films")
                        .content(gson.toJson(filmFailReleaseDate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Дата релиза не должна быть раньше 28 декабря 1895г"));
    }

    @Test
    @DirtiesContext
    void testAddFailDuration() throws Exception {
        mockMvc.perform(post("/films")
                        .content(gson.toJson(filmFailDuration))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Продолжительность не может быть отрицательная"));
    }

    @Test
    @DirtiesContext
    void testAddEmptyBody() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DirtiesContext
    void testUpdate() throws Exception {
        updatedFilm.setId(1L);
        mockMvc.perform(post("/films")
                .content(gson.toJson(film))
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updatedFilm)))
                .andExpect(status().isOk())
                .andExpect(result -> gson.toJson(updatedFilm));
    }

    @Test
    @DirtiesContext
    void testUpdateFailId() throws Exception {
        updatedFilm.setId(-1L);
        mockMvc.perform(post("/films")
                .content(gson.toJson(film))
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updatedFilm)))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Фильм с таким id не существует"));
    }

    @Test
    @DirtiesContext
    void testUpdateEmptyBody() throws Exception {
        mockMvc.perform(post("/films")
                .content(gson.toJson(film))
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
