package ru.yandex.praktikum.user.User;

public class UserData {
    public final String email;
    public final String password;

    public UserData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserData from(User user) {
        return new UserData(user.email, user.password);
    }
}
