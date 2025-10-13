package praktikum.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;

    // Генерация случайного пользователя
    public static User getRandomUser() {
        String unique = UUID.randomUUID().toString().substring(0, 6);
        return new User(
                "test_" + unique + "@yandex.ru",
                "password123",
                "User_" + unique
        );
    }
}

