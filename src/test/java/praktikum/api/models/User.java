package praktikum.api;

import java.util.UUID;

public class User {

    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return  name;
    }

    public static User getRandomUser() {
        String unique = UUID.randomUUID().toString().substring(0, 6);
        return new User(
                "test_" + unique + "@yandex.ru",
                "password123",
                "User_" + unique
        );
    }
}
