package praktikum.api;

import io.qameta.allure.Step;


import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    @Step("Создание пользователя")
    public io.restassured.response.Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + "/auth/register");
    }

    @Step("Логин пользователя")
    public io.restassured.response.Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + "/auth/login");
    }

    @Step("Удаление пользователя")
    public void delete(String accessToken) {
        if (accessToken != null) {
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(BASE_URL + "/auth/user");
        }
    }
}

