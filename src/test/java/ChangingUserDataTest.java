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

public class ChangingUserDataTest extends BaseSpec {

    public String accessToken;
    private User user;
    private UserClient userClient;
    private UserData userData;

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
    @DisplayName("Изменения регистрационных данных с авторизацией")
    public void editUserDataWithAuthorizationTest() {
        String accessToken = userClient.registrationAndGetAccessToken(user);
        userClient.Authorization(UserData.from(user));
        User newUser = User.getRandom();
        userClient.editUserData(newUser, accessToken)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }


    @Test
    @DisplayName("Изменения регистрационных данных без авторизации")
    public void editUserDataWithoutAuthorizationTest() {
        userClient.create(user);
        User newUser = User.getRandom();
        userClient.editUserDataWithoutLogin(UserData.from(newUser))
                .then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }


}
