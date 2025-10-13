package praktikum.api.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import praktikum.api.clients.UserClient;
import praktikum.api.models.User;
import praktikum.api.generators.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {

    private final UserClient client = new UserClient();
    private User testUser;

    @After
    public void tearDown() {
        if (testUser != null) {
            String token = client.login(testUser)
                    .then()
                    .extract()
                    .path("accessToken");

            client.delete(token);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешной регистрации нового пользователя с помощью JavaFaker")
    public void createUniqueUserTest() {
        testUser = UserGenerator.getRandomUser();
        client.create(testUser)
                .then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка, что нельзя зарегистрировать одного и того же пользователя дважды")
    public void createExistingUserTest() {
        testUser = UserGenerator.getRandomUser();
        client.create(testUser).then().statusCode(SC_OK);

        client.create(testUser)
                .then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка, что если не указать email — вернется 403")
    public void createUserWithoutEmailTest() {
        testUser = new User("", "password123", "TestUser");
        client.create(testUser)
                .then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка, что если не указать password — вернется 403")
    public void createUserWithoutPasswordTest() {
        testUser = new User("test@example.com", "", "TestUser");
        client.create(testUser)
                .then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка, что если не указать name — вернется 403")
    public void createUserWithoutNameTest() {
        testUser = new User("test@example.com", "password123", "");
        client.create(testUser)
                .then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}



