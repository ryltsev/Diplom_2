package ru.yandex.praktikum.user.User;

import io.qameta.allure.Step;
import ru.yandex.praktikum.user.BaseSpec;

import static io.restassured.RestAssured.given;

public class IngredientClient extends BaseSpec {
    public final static String APIURL = "/ingredients/";
    public final static String Type = "Content-type";
    public final static String APP = "application/json";

    @Step("Получение списка ингредиентов")
    public String getIngredients(int massiveId) {
        return given()
                .spec(getBaseSpec())
                .get(APIURL)
                .then()
                .extract()
                .path("data[" + massiveId + "]._id");
    }
}
