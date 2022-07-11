package ru.yandex.praktikum.user.User;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.user.BaseSpec;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseSpec {
    public final static String APIURL = "/auth/";

    @Step("Регистрация пользователя")
    public Response create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(APIURL + "register/");
    }

    @Step("Регистрация пользователя без почты")
    public Response createWithOutEmail(User user) {
        String body = "{\"password\":\"" + user.password + "\","
                + "\"name\":\"" + user.name + "\"}";

        return given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(APIURL + "register/");
    }

    @Step("Регистрация пользователя без пароля")
    public Response createWithOutPassword(User user) {
        String body = "{\"email\":\"" + user.email + "\","
                + "\"name\":\"" + user.name + "\"}";
        return given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(APIURL + "register/");
    }

    @Step("Регистрация пользователя без имени")
    public Response createWithOutName(User user) {
        String body = "{\"email\":\"" + user.email + "\","
                + "\"password\":\"" + user.password + "\"}";
        return given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(APIURL + "register/");
    }

    @Step("Авторизация пользователя")
    public Response Authorization(UserData userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post(APIURL + "login/");
    }

    @Step("Авторизация пользователя с неверным логином и паролем")
    public Response userAuthorizationWithIncorrectEmailPassword(UserData userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post(APIURL + "login/");
    }

    @Step("Получение на accessToken после авторизации")
    public String getUserAccessToken(UserData userData) {
        return given()
                .spec(getBaseSpec())
                .body(userData)
                .when()
                .post(APIURL + "/login/")
                .then()
                .extract()
                .path("accessToken")
                .toString()
                .substring(7);
    }

    @Step("Регистрация и получение accessToken")
    public String registrationAndGetAccessToken(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(APIURL + "register/")
                .then()
                .extract()
                .path("accessToken")
                .toString()
                .substring(7);
    }

    @Step("Изменение данных пользователя")
    public Response editUserData(User newUser, String accessToken) {
        String body = "{ \"email\":\"" + newUser.email + "\","
                + "\"name\":\"" + newUser.name + "\"}";
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .patch(APIURL + "user/");
    }


    @Step("Изменение данных неавторизованного пользователя")
    public Response editUserDataWithoutLogin(UserData user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch(APIURL + "user/");
    }
}
