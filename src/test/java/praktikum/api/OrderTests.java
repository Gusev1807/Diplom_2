package praktikum.api;

import org.junit.Before;

import java.util.List;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import io.restassured.response.Response;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTests {

    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;

    private List<String> ingredientIds;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();

        // Создание пользователя
        User user = User.getRandomUser();
        Response response = userClient.create(user);
        accessToken = response.path("accessToken");

        // получаем список ингредиентов
        Response ingredientsResponse = orderClient.getIngredients();
        ingredientIds = ingredientsResponse.path("data._id");
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверка, что авторизованный пользователь может создать заказ")
    public void createOrderWithAuthAndIngredients() {
        Response response = orderClient.createOrder(accessToken,
                Collections.singletonMap("ingredients", ingredientIds.subList(0, 2)));

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка, что можно создать заказ и без авторизации")
    public void createOrderWithoutAuth() {
        Response response = orderClient.createOrder("",
                Collections.singletonMap("ingredients", ingredientIds.subList(0, 2)));

        response.then()
                .statusCode(HttpStatus.SC_OK) // по документации — это ок
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при попытке создать заказ без ингредиентов")
    public void createOrderWithoutIngredients() {
        Response response = orderClient.createOrder(accessToken,
                Collections.singletonMap("ingredients", Collections.emptyList()));

        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка ошибки при создании заказа с невалидными id ингредиентов")
    public void createOrderWithInvalidIngredients() {
        // передаем "невалидный" хеш ингредиента
        List<String> invalidIds = Collections.singletonList("61c0c5a71d1f82001bdaaa6j");

        Response response = orderClient.createOrder(accessToken,
                Collections.singletonMap("ingredients", invalidIds));

        // проверяем, что сервер вернул 500 Internal Server Error
        response.then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
