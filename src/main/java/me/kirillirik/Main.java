package me.kirillirik;

import me.kirillirik.database.Database;
import me.kirillirik.manulab.Manulab;

public final class Main {
    public static void main(String[] args) {
        Database.init();

        final Window window = new Window(new Manulab());
        window.init();
        window.run();
        window.destroy();

        Database.close();
    }
}