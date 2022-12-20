package me.kirillirik.manulab;

import imgui.ImGui;
import me.kirillirik.manulab.auth.Auth;
import me.kirillirik.manulab.main.Editor;
import me.kirillirik.texture.Texture;

public final class Manulab {

    private static State state;
    private static Texture texture;
    private final Auth auth;
    private final Editor editor;

    public Manulab() {
        state = State.AUTH;

        auth = new Auth();
        editor = new Editor();
    }

    public void tryLoadSession() {
        auth.tryLoadSession();
    }

    public void update() {
        switch (state) {
            case AUTH -> auth.update();
            case MAIN_MANUL -> editor.update();
        }
    }

    public static void setState(State state) {
        Manulab.state = state;

        switch (state) {
            case MAIN_MANUL -> Editor.setState(Editor.State.EMPTY);
        }
    }

    public static State getState() {
        return state;
    }

    public static void drawLogo() {
        ImGui.getWindowDrawList().addImage(texture.getId(),
                ImGui.getWindowContentRegionMaxX() / 2 - texture.getWidth() / 2f, ImGui.getWindowContentRegionMaxY() / 2 - texture.getHeight() / 2f,
                ImGui.getWindowContentRegionMaxX() / 2 + texture.getWidth() / 2f, ImGui.getWindowContentRegionMaxY() / 2 + texture.getHeight() / 2f,
                0, 0, 1, 1);
    }

    public static void drawLogo2() {
        ImGui.getBackgroundDrawList().addImage(texture.getId(),
                ImGui.getWindowWidth() / 2 - texture.getWidth() / 2f, ImGui.getWindowHeight() / 2 - texture.getHeight() / 2f,
                ImGui.getWindowWidth() / 2 + texture.getWidth() / 2f, ImGui.getWindowHeight() / 2 + texture.getHeight() / 2f,
                0, 0, 1, 1);
    }

    public static void loadTexture() {
        Manulab.texture = Texture.create("Manulab.png");
    }

    public enum State {
        AUTH,
        MAIN_MANUL,
        CLOSE
    }
}