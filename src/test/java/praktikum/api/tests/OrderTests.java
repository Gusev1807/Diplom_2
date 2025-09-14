package praktikum.api.tests;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import io.restassured.response.Response;
import praktikum.api.models.User;
import praktikum.api.steps.UserSteps;
import praktikum.api.steps.OrderSteps;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTests {

    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String accessToken;
    private List<String> ingredientIds;
    private User testUser;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();

        // Создание пользователя
        testUser = User.getRandomUser();
        Response response = userSteps.createUser(testUser);
        accessToken = response.path("accessToken");

        // Получение ингредиентов
        ingredientIds = orderSteps.getIngredients().path("data._id");
    }

    @After
    public void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверка, что авторизованный пользователь может создать заказ")
    public void createOrderWithAuthAndIngredients() {
        Response response = orderSteps.createOrder(accessToken, ingredientIds.subList(0, 2));
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка, что можно создать заказ и без авторизации")
    public void createOrderWithoutAuth() {
        Response response = orderSteps.createOrder("", ingredientIds.subList(0, 2));
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при попытке создать заказ без ингредиентов")
    public void createOrderWithoutIngredients() {
        Response response = orderSteps.createOrder(accessToken, Collections.emptyList());
        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }
}

