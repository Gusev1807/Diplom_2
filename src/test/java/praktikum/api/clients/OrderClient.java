package praktikum.api.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.api.Endpoints;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    @Step("Создание заказа")
    public Response createOrder(String accessToken, Object orderBody) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(orderBody)
                .when()
                .post(Endpoints.ORDERS);
    }

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(Endpoints.INGREDIENTS);
    }
}


