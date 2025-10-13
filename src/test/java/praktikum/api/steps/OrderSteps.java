package praktikum.api.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.api.clients.OrderClient;

import java.util.List;
import java.util.Map;

public class OrderSteps {

    private final OrderClient orderClient = new OrderClient();

    @Step("Создать заказ с токеном {accessToken} и ингредиентами {ingredients}")
    public Response createOrder(String accessToken, List<String> ingredients) {
        return orderClient.createOrder(accessToken, Map.of("ingredients", ingredients));
    }

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return orderClient.getIngredients();
    }
}

