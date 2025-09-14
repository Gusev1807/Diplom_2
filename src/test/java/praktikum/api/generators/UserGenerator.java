package praktikum.api.generators;

import praktikum.api.models.User;
import com.github.javafaker.Faker;

public class UserGenerator {

    private static final Faker faker = new Faker();

    public static User getRandomUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 12); // длина пароля от 8 до 12
        String name = faker.name().firstName();

        return new User(email, password, name);
    }
}

