package me.kirillirik.manulab.auth;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import me.kirillirik.User;
import me.kirillirik.database.Database;
import me.kirillirik.util.PasswordUtil;

import java.sql.ResultSet;

public final class Auth {
    private static User user;
    private final ImString login;
    private final ImString password;
    private final ImString passwordAgain;
    private String info;
    private State state;

    public Auth() {
        user = null;

        this.login = new ImString();
        this.password = new ImString();
        this.passwordAgain = new ImString();

        info = null;
        state = State.AUTH;
    }

    public void update() {
        ImGui.begin(state.name + "###auth_window");
        if (info != null) {
            ImGui.text(info);
        }
        switch (state) {
            case AUTH -> auth();
            case REGISTER -> register();
        }
        ImGui.end();
    }

    private void auth() {
        ImGui.text("Введите логин");
        ImGui.inputText("##Логин", login);

        ImGui.text("Введите пароль");
        ImGui.inputText("##Пароль", password, ImGuiInputTextFlags.Password);

        final boolean empty = login.isEmpty() || password.isEmpty();

        final String text = empty ? "Введите логин и пароль" : "Авторизоваться";

        ImGui.beginDisabled(empty);
        if (ImGui.button(text)) {
            user = loadUser();

            if (user != null) {

            } else {
                info = "Неверный логин или пароль!";
            }
        }
        ImGui.endDisabled();

        ImGui.sameLine();
        if (ImGui.button("Регистрация")) {
            state = State.REGISTER;

            login.clear();
            password.clear();
            info = null;
        }
    }

    private void register() {
        ImGui.text("Введите логин");
        ImGui.inputText("##login", login);

        ImGui.text("Введите пароль");
        ImGui.inputText("##password", password, ImGuiInputTextFlags.Password);

        ImGui.text("Повторите пароль");
        ImGui.inputText("##password_again", passwordAgain, ImGuiInputTextFlags.Password);

        final boolean empty = login.isEmpty() || password.isEmpty();
        final boolean equalsPassword = !empty && password.get().equals(passwordAgain.get());
        if (empty || !equalsPassword) {
            ImGui.beginDisabled();
        }

        final String text = empty ? "Введите логин и пароль" : (equalsPassword ? "Зарегистрироваться" : "Пароли не совпадают");
        if (ImGui.button(text)) {
            if (checkIfHasLogin()) {
                info = "Такой логин уже существует!";
            } else {
                registerNewUser();

                info = "Вы успешно зарегистрировались!";
                state = State.AUTH;

                login.clear();
                password.clear();
                passwordAgain.clear();
            }
        }

        if (empty || !equalsPassword) {
            ImGui.endDisabled();
        }


        ImGui.sameLine();
        if (ImGui.button("Вернуться к авторизации")) {
            state = State.AUTH;

            login.clear();
            password.clear();
            passwordAgain.clear();
            info = null;
        }
    }


    private User loadUser() {
        return Database.sync().rs("select * from \"user\" where login = ?", rs -> {
            while (rs.next()) {
                final String hashedPassword = PasswordUtil.hashPassword(password.get(), rs.getString("salt"));
                if (hashedPassword.equals(rs.getString("password"))) {
                    return new User(login.get(), rs.getString("role"), rs.getInt("collector_id"));
                }
            }

            return null;

        }, login.get());
    }

    private boolean checkIfHasLogin() {
        return Database.sync().rs("select * from \"user\" where login = ?", ResultSet::next, login.get());
    }

    private void registerNewUser() {
        final String salt = PasswordUtil.generateSalt();
        final String hash = PasswordUtil.hashPassword(password.get(), salt);
        Database.sync().insert("insert into \"user\"(login, password, salt, role) values(?, ?, ?, ?)",
                rs -> null, login.get(), hash, salt, "user");
    }

    public static User user() {
        return user;
    }

    public enum State {
        AUTH("Авторизация"),
        REGISTER("Регистрация");

        private final String name;
        State(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
