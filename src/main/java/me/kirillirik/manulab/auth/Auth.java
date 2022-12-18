package me.kirillirik.manulab.auth;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import me.kirillirik.User;
import me.kirillirik.database.Database;
import me.kirillirik.manulab.Manulab;
import me.kirillirik.util.PasswordUtil;

import java.io.*;
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

    public void tryLoadSession() {
        if (loadSession()) {
            Manulab.setState(Manulab.State.MAIN_MANUL);
        }
    }

    public void update() {
        ImGui.begin(state.name + "###auth_window");
        if (info != null) {
            ImGui.textColored(150, 0, 0, 255, info);
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
                clearData();

                Manulab.setState(Manulab.State.MAIN_MANUL);
            } else {
                info = "Неверный логин или пароль!";
            }
        }
        ImGui.endDisabled();

        ImGui.sameLine();
        if (ImGui.button("Регистрация")) {
            state = State.REGISTER;

            clearData();
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

                state = State.AUTH;
                info = "Вы успешно зарегистрировались!";

                clearData();
            }
        }

        if (empty || !equalsPassword) {
            ImGui.endDisabled();
        }


        ImGui.sameLine();
        if (ImGui.button("Вернуться к авторизации")) {
            clearData();

            state = State.AUTH;
            info = null;
        }
    }

    private boolean loadSession() {
        final File file = new File("session.dat");
        if (!file.exists()) {
            return false;
        }

        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));

            final Boolean verifiedPassword = Database.sync().rs("select * from \"user\" where login = ? and password = ?",
                    ResultSet::next, reader.readLine(), reader.readLine());

            final boolean result = verifiedPassword != null && verifiedPassword;

            if (!result) {
                info = "Не удалось восстановить сессию!";
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private User loadUser() {
        return Database.sync().rs("select * from \"user\" where login = ?", rs -> {
            while (rs.next()) {
                final String hashedPassword = PasswordUtil.hashPassword(password.get(), rs.getString("salt"));

                if (hashedPassword.equals(rs.getString("password"))) {

                    try {
                        final File file = new File("session.dat");
                        file.createNewFile();

                        final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                        writer.write(login.get());
                        writer.newLine();
                        writer.write(hashedPassword);

                        writer.close();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return new User(rs.getInt("id"), login.get(), rs.getString("role"), rs.getInt("collector_id"));
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

    private void clearData() {
        login.clear();
        password.clear();
        passwordAgain.clear();
    }

    public static User user() {
        return user;
    }

    public static void logout() {
        user = null;

        clearSession();
    }

    public static void clearSession() {
        final File file = new File("session.dat");
        if (file.exists()) {
            file.delete();
        }
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
