package me.kirillirik.manulab.main;

import imgui.ImGui;
import me.kirillirik.manulab.auth.Auth;

public final class MainMenu {

    public MainMenu() {

    }

    public void update() {
        ImGui.begin("Манул###main_menu");
        ImGui.text("Вы вошли как " + Auth.user().getLogin() + "!");

        ImGui.end();
    }
}
