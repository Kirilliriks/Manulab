package me.kirillirik.manulab;

import me.kirillirik.manulab.auth.Auth;
import me.kirillirik.manulab.main.MainMenu;

public final class Manulab {

    private static State state;
    private final Auth auth;
    private final MainMenu mainMenu;

    public Manulab() {
        state = State.AUTH;

        auth = new Auth();
        mainMenu = new MainMenu();
    }

    public void update() {
        switch (state) {
            case AUTH -> auth.update();
            case MAIN_MANUL -> mainMenu.update();
        }
    }

    public static void setState(State state) {
        Manulab.state = state;
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