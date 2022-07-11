package ru.yandex.praktikum.user.User;

import org.apache.commons.lang3.RandomStringUtils;

public class User {

    public String email;
    public String password;
    public String name;

    //конструктор пользователя
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    //метод для генерации пользователя со случайными данными
    public static User getRandom() {
        final String email = RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(5) + ".com";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }
}
