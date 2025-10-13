package praktikum.api.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.api.clients.UserClient;
import praktikum.api.models.User;

public class UserSteps {

    private final UserClient userClient = new UserClient();

    @Step("Создать пользователя {user.email}")
    public Response createUser(User user) {
        return userClient.create(user);
    }

    @Step("Удалить пользователя с токеном {accessToken}")
    public void deleteUser(String accessToken) {
        userClient.delete(accessToken);
    }
}

