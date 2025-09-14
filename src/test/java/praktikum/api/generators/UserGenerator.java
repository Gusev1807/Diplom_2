package praktikum.api;

import praktikum.api.models.User;

import java.util.UUID;

public class UserGenerator {

    public static User getRandomUser() {
        String email = "User_" + UUID.randomUUID() + "@yandex.ru";
        String password = "password123";
        String name = "TestUser";
        return new User(email, password, name);
    }
}
