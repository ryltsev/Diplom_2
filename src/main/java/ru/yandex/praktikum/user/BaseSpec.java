package ru.yandex.praktikum.user;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseSpec {
    public final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    protected RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder().setContentType(ContentType.JSON).setBaseUri(BASE_URL).build();
    }
}
