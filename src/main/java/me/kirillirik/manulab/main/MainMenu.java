package me.kirillirik.manulab.main;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import me.kirillirik.Window;
import me.kirillirik.manulab.Manulab;
import me.kirillirik.manulab.auth.Auth;
import me.kirillirik.manulab.main.table.Table;

public final class MainMenu {

    private State state;
    private Table<?> table;

    public MainMenu() {
        state = State.EMPTY;
        table = null;
    }

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
                Manulab.setState(Manulab.State.AUTH);
                Auth.logout();
            }

            if (ImGui.menuItem("Закрыть программу")) {
                Manulab.setState(Manulab.State.CLOSE);
            }

            ImGui.endMenu();
        }


        if (ImGui.beginMenu("Выбрать таблицу")) {

            for (final TableType type : TableType.values()) {
                if (ImGui.menuItem(type.getName())) {
                    table = type.getTable();

                    table.refresh(false);

                    state = State.VIEW_TABLE;
                }
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();

        switch (state) {
            case VIEW_TABLE -> {
                ImGui.text("Таблица " + table.getType().getName() + (table.isDirty() ? " - [Не сохранено]" : ""));
                table.update();
            }
        }

        ImGui.end();
    }

    public enum State {
        EMPTY,
        VIEW_TABLE
    }
}
