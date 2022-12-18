package me.kirillirik;

import me.kirillirik.database.Database;

public final class Main {
    public static void main(String[] args) {
        Database.init();

        final Window window = new Window();
        window.init();
        window.run();
        window.destroy();

        Database.close();

        System.exit(0);
    }
}