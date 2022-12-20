package me.kirillirik.manulab;

import me.kirillirik.manulab.auth.Auth;
import me.kirillirik.manulab.main.Editor;

public final class Manulab {

    private static State state;
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

    public enum State {
        AUTH,
        MAIN_MANUL,
        CLOSE
    }
}