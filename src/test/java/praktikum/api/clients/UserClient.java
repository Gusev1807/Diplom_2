package praktikum.api.clients;

import io.qameta.allure.Step;
import praktikum.api.Endpoints;
import praktikum.api.models.User;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Создание пользователя")
    public io.restassured.response.Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(Endpoints.REGISTER);
    }

    @Step("Логин пользователя")
    public io.restassured.response.Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(Endpoints.LOGIN);
    }

    @Step("Удаление пользователя (только для очистки, не проверяем результат)")
    public void delete(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) return;

        // Просто выполняем запрос, не проверяем статус
        given()
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.DELETE_USER);
    }
}





