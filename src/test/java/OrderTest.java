import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.user.BaseSpec;
import ru.yandex.praktikum.user.User.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTest extends BaseSpec {

    public String accessToken;
    private UserClient userClient;
    private OrderClient orderClient;
    private IngredientClient ingredientClient;
    private User user;

    public String getAccessToken() {
        {
            return accessToken;
        }
    }


    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        ingredientClient = new IngredientClient();
        user = User.getRandom();
    }

    @After
    public void delete() {
        if (getAccessToken() == null) {
            return;
        }
        given()
                .spec(getBaseSpec())
                .auth().oauth2(getAccessToken().substring(7))
                .when()
                .delete("auth/user")
                .then()
                .statusCode(202);
    }

    @Test
    @DisplayName("Авторизация пользователя и создание заказа с ингредиентами")
    public void createOrderAuthorizedUserTest() {
        userClient.create(user);
        String accessToken = userClient.getUserAccessToken(UserData.from(user));
        String ingredient = ingredientClient.getIngredients(1);
        orderClient.newOrderWithAuthorization(ingredient, accessToken)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа с игредиентами без авторизации пользователя")
    public void createOrderWithIngredientAndWithoutAuthorizationTest() {
        String ingredientId = ingredientClient.getIngredients(1);
        orderClient.newOrderWithoutAuthorization(ingredientId)
                .then().assertThat()
                .body("success", equalTo(false))
                .and()
                .statusCode(400);

    }

    @Test
    @DisplayName("Создание заказа без игредиентов с авторизацией пользователя")
    public void createOrderWithoutIngredientAndWithAuthorizationTest() {
        userClient.create(user);
        String accessToken = userClient.getUserAccessToken(UserData.from(user));
        orderClient.newOrderWithoutIngredients(accessToken)
                .then().assertThat()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией пользователя и с неверным хешем")
    public void createOrderWithWrongIngredientAndWithAuthorizationTest() {
        userClient.create(user);
        String accessToken = userClient.getUserAccessToken(UserData.from(user));
        String ingredientId = "incorrectId";
        orderClient.newOrderWithAuthorization(ingredientId, accessToken)
                .then().assertThat()
                .statusCode(500);

    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrderAfterAuthorizationTest() {
        userClient.create(user);
        String accessToken = userClient.getUserAccessToken(UserData.from(user));
        String ingredient = ingredientClient.getIngredients(1);
        orderClient.newOrderWithAuthorization(ingredient, accessToken);
        orderClient.getOrdersList(accessToken)
                .then().assertThat()
                .body("total", notNullValue())
                .and()
                .body("orders", notNullValue())
                .and()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Попытка получения заказов неавторизованного пользователя")
    public void getOrderWithoutAuthorizationTest() {
        orderClient.getOrdersListWithoutUserAuthorization()
                .then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);

    }
}
