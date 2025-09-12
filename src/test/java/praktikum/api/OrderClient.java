package praktikum.api;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

@Step("Создание заказа")
    public io.restassured.response.Response createOrder(String accessToken, Object orderBody) {
    return given()
            .header("Content-type", "application/json")
            .header("Authorization", accessToken)
            .body(orderBody)
            .when()
            .post(BASE_URL + "/orders");
}

@Step("Получение списка ингредиентов")

    public io.restassured.response.Response getIngredients() {
    return given()
            .header("Content-type", "application/json")
            .when()
            .get(BASE_URL + "/ingredients");
}
}
