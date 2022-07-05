import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.user.User.User;
import ru.yandex.praktikum.user.User.UserClient;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserRegistrationTest {

    private User user;
    private UserClient userClient;

    @Before
    public void SetUp() {
        user = User.getRandom();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    public void newUserRegistrationTest() {
        userClient.create(user)
                .then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Повторная регистрация нового пользователя с уже зарегистрированными данными")
    public void reRegistrationUserTest() {
        userClient.create(user);
        userClient.create(user)
                .then().assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без указания почты")
    public void newUserWithoutEmailTest() {
        userClient.createWithOutEmail(user)
                .then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без указания пароля")
    public void newUserWithoutPasswordTest() {
        userClient.createWithOutPassword(user)
                .then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без указания имени")
    public void newUserWithoutNameTest() {
        userClient.createWithOutName(user)
                .then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(403);
    }
}
