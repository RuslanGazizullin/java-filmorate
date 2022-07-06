package ru.yandex.praktikum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.praktikum.filmorate.exception.ValidationException;
import ru.yandex.praktikum.filmorate.model.User;
import ru.yandex.praktikum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserStorage userStorage;

    public User addFriend(Long id, Long friendId) throws ObjectNotFoundException, ValidationException {
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(friendId)) {
            User user = userStorage.findById(id);
            User friend = userStorage.findById(friendId);
            user.getFriends().add(friendId);
            friend.getFriends().add(id);
            userStorage.update(user);
            userStorage.update(friend);
            log.info("Пользователь " + friend.getName() + " успешно добавлен в друзья");
            return friend;
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public User deleteFriend(Long id, Long friendId) throws ObjectNotFoundException, ValidationException {
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(friendId) &&
                user.getFriends().contains(friendId)) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(id);
            userStorage.update(user);
            userStorage.update(friend);
            log.info("Пользователь " + friend.getName() + " успешно удален из друзей");
            return friend;
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public List<User> findCommonFriends(Long id, Long otherId) throws ObjectNotFoundException {
        if (userStorage.getUsers().containsKey(id) && userStorage.getUsers().containsKey(otherId)) {
            List<User> commonFriends = findFriends(id);
            commonFriends.retainAll(findFriends(otherId));
            log.info("Список общих друзей сформирован");
            return commonFriends;
        } else {
            throw new ObjectNotFoundException("Пользователи не найдены");
        }
    }

    public List<User> findFriends(Long id) throws ObjectNotFoundException {
        List<User> friends = new ArrayList<>();
        List<Long> friendsId = new ArrayList<>(userStorage.findById(id).getFriends());
        for (Long friendId : friendsId) {
            friends.add(userStorage.findById(friendId));
        }
        log.info("Список друзей сформирован");
        return friends;
    }
}
