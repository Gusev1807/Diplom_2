package praktikum.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {

    private final UserClient client = new UserClient();
    private String accessToken;

    @After
    public void tearDown() {
        client.delete(accessToken); // Удаление пользователя после теста
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешной регистрации нового пользователя")
    public void createUniqueUserTest() {
        User user = UserGenerator.getRandomUser();
        var response = client.create(user);

        response.then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        accessToken = response.then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка, что нельзя зарегистрировать одного и того же пользователя дважды")
    public void createExistingUserTest() {
        User user = UserGenerator.getRandomUser();
        var firstResponse = client.create(user);
        accessToken = firstResponse.then().extract().path("accessToken");

        var secondResponse = client.create(user);

        secondResponse.then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    @Description("Проверка, что если не указать email, password или name — вернется 403")
    public void createUserWithoutRequiredFieldTest() {
        User user = new User("", "password123", "TestUser"); // нет email
        var response = client.create(user);

        response.then().statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}

