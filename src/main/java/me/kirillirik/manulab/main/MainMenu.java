package me.kirillirik.manulab.main;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import me.kirillirik.Window;
import me.kirillirik.manulab.auth.Auth;

public final class MainMenu {

    public MainMenu() {

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
        ImGui.text("Вы вошли как " + Auth.user().getLogin() + "!");

        ImGui.end();
    }
}
