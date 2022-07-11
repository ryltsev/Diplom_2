import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.user.BaseSpec;
import ru.yandex.praktikum.user.User.User;
import ru.yandex.praktikum.user.User.UserClient;
import ru.yandex.praktikum.user.User.UserData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserAuthorizationTest extends BaseSpec {

    public String accessToken;
    private User user;
    private UserClient userClient;

    public String getAccessToken() {
        {
            return accessToken;
        }
    }

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
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
    @DisplayName("Авторизация существующего пользователя")
    public void authorizationRegisteredUser() {
        userClient.create(user);
        userClient.Authorization(UserData.from(user))
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("логин с неверным логином и паролем")
    public void authorizationWithIncorrectLoginPassword() {
        userClient.userAuthorizationWithIncorrectEmailPassword(UserData.from(user))
                .then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and().
                statusCode(401);
    }
}
