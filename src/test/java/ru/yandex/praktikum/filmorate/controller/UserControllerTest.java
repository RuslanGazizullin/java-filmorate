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
import ru.yandex.praktikum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    User user = new User("user@user.ru", "user", "User_name", LocalDate.of(1986, 2, 19));
    User updatedUser = new User("user@user.ru", "user", "Updated_user_name", LocalDate.of(1986, 2, 19));
    User userFailEmail = new User("user.user.ru", "user", "User_name", LocalDate.of(1986, 2, 19));
    User userEmptyEmail = new User("", "user", "User_name", LocalDate.of(1986, 2, 19));
    User userFailLogin = new User("user@user.ru", "user 1", "User_name", LocalDate.of(1986, 2, 19));
    User userEmptyLogin = new User("user@user.ru", "", "User_name", LocalDate.of(1986, 2, 19));
    User userEmptyName = new User("user@user.ru", "user", "", LocalDate.of(1986, 2, 19));
    User userFailBirthday = new User("user@user.ru", "user", "User_name", LocalDate.of(2086, 2, 19));

    @Test
    @DirtiesContext
    void testFindAll() throws Exception {
        ArrayList<User> users = new ArrayList<>();
        user.setId(1);
        users.add(user);
        mockMvc.perform(post("/users")
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(get("/users"))
                .andExpect(result -> gson.toJson(users))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void testFindAllNoFilms() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(content().string("[]"))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void testAdd() throws Exception {
        mockMvc.perform(post("/users")
                        .content(gson.toJson(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> gson.toJson(user));
    }

    @Test
    @DirtiesContext
    void testAddEmptyEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .content(gson.toJson(userEmptyEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Электронная почта отсутствует или неверный формат"));
    }

    @Test
    @DirtiesContext
    void testAddFailEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .content(gson.toJson(userFailEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Электронная почта отсутствует или неверный формат"));
    }

    @Test
    @DirtiesContext
    void testAddEmptyLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .content(gson.toJson(userEmptyLogin))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Логин не может быть пустым или содержать пробелы"));
    }

    @Test
    @DirtiesContext
    void testAddFailLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .content(gson.toJson(userFailLogin))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Логин не может быть пустым или содержать пробелы"));
    }

    @Test
    @DirtiesContext
    void testAddFailBirthday() throws Exception {
        mockMvc.perform(post("/users")
                        .content(gson.toJson(userFailBirthday))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Неправильная дата рождения"));
    }

    @Test
    @DirtiesContext
    void testAddEmptyName() throws Exception {
        User userWithEmptyName = userEmptyName;
        userWithEmptyName.setName(userEmptyName.getLogin());
        mockMvc.perform(post("/users")
                        .content(gson.toJson(userEmptyName))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> gson.toJson(userWithEmptyName));
    }

    @Test
    @DirtiesContext
    void testAddEmptyBody() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DirtiesContext
    void testUpdate() throws Exception {
        updatedUser.setId(1);
        mockMvc.perform(post("/users")
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(result -> gson.toJson(updatedUser));
    }

    @Test
    @DirtiesContext
    void testUpdateFailId() throws Exception {
        updatedUser.setId(-1);
        mockMvc.perform(post("/users")
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updatedUser)))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Пользователь с таким id не существует"));
    }

    @Test
    @DirtiesContext
    void testUpdateEmptyBody() throws Exception {
        mockMvc.perform(post("/users")
                .content(gson.toJson(user))
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
