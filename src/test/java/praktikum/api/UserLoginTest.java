package praktikum.api;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {

    private final UserClient userClient = new UserClient();
    private User testUser;
    private String accessToken;

    @Before
    public void setUp() {
        testUser = UserGenerator.getRandomUser();
        userClient.create(testUser).then().statusCode(SC_OK);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Успешный логин с существующим пользователем")
    @Description("Ожидаем статус-код 200 и success=true")
    public void loginWithValidUserTest() {
        accessToken = userClient.login(testUser)
                .then().statusCode(SC_OK)
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Ожидаем статус-код 401 и сообщение об ошибке")
    public void loginWithInvalidPasswordTest() {
        User wrongUser = new User(testUser.getEmail(), "wrongPassword", testUser.getName());

        userClient.login(wrongUser)
                .then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("Ожидаем статус-код 401 и сообщение об ошибке")
    public void loginWithInvalidEmailTest() {
        User wrongUser = new User("wrong_" + testUser.getEmail(), testUser.getPassword(), testUser.getName());

        userClient.login(wrongUser)
                .then().statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}


