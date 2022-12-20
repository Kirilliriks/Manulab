package me.kirillirik.manulab.main;

import imgui.ImGui;
import imgui.flag.*;
import imgui.type.ImBoolean;
import me.kirillirik.User;
import me.kirillirik.Window;
import me.kirillirik.manulab.Manulab;
import me.kirillirik.manulab.auth.Auth;
import me.kirillirik.manulab.auth.Role;
import me.kirillirik.manulab.main.table.Table;
import me.kirillirik.texture.Texture;

public final class Editor {

    private static State state;
    private static Texture texture;
    private Table<?> table;

    public Editor() {
        state = State.EMPTY;
        table = null;
    }

    float[] color = new float[3];

    public void update() {
        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        final int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("###main_menu", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);


        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("Меню")) {

            if (ImGui.menuItem("Выход из аккаунта")) {
                Auth.logout();
                Manulab.setState(Manulab.State.AUTH);
            }

            if (ImGui.menuItem("Закрыть программу")) {
                Manulab.setState(Manulab.State.CLOSE);
            }

            ImGui.endMenu();
        }


        final User user = Auth.user();
        if (user != null) {
            if (!user.getRole().getTypeName().equalsIgnoreCase("user")) {
                selectTablesFor(user);

                ImGui.textColored(255, 69, 48, 180, "Пользователь " + Auth.user().getLogin() + " авторизован как " + Auth.user().getRole().getTypeName());
            } else {
                ImGui.textColored(255, 69, 48, 180, "Попросите администратора выдать вам необходимый доступ");
            }
        }


        ImGui.endMainMenuBar();

        ImGui.setCursorScreenPos(0, 0);
        ImGui.getWindowDrawList().addImage(texture.getId(),
                ImGui.getWindowContentRegionMaxX() / 2 - texture.getWidth() / 2f, ImGui.getWindowContentRegionMaxY() / 2 - texture.getHeight() / 2f,
                ImGui.getWindowContentRegionMaxX() / 2 + texture.getWidth() / 2f, ImGui.getWindowContentRegionMaxY() / 2 + texture.getHeight() / 2f,
                0, 0, 1, 1);

        switch (state) {
            case EMPTY -> { }
            case VIEW_TABLE -> {
                ImGui.text("Таблица " + table.getType().getName() + (table.isDirty() ? " - [Не сохранено]" : ""));
                table.update();
            }
        }

        ImGui.end();
    }

    private void selectTablesFor(User user) {
        if (ImGui.beginMenu("Выбрать таблицу")) {
            for (final TableType type : TableType.values()) {
                final Role role = type.getRole(user.getRole().getTypeName());
                if (role == null) {
                    continue;
                }

                if (ImGui.menuItem(type.getName())) {
                    table = type.getTable();
                    table.refresh(false);

                    state = State.VIEW_TABLE;
                }
            }

            ImGui.endMenu();
        }
    }

    public static void loadTexture() {
        Editor.texture = Texture.create("Manulab.png");
    }

    public static void setState(State state) {
        Editor.state = state;
    }

    public enum State {
        EMPTY,
        VIEW_TABLE
    }
}
