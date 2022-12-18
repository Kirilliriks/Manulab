package me.kirillirik.manulab.main.table.user;

import imgui.type.ImInt;
import imgui.type.ImString;
import me.kirillirik.manulab.main.table.TableRow;

public final class UserRow extends TableRow {

    private final int id;
    private final ImString login;
    private final ImString newPassword;
    private final ImString newSalt;
    private final ImString role;
    private final ImInt collectorID;

    public UserRow(int id, String login, String role, int collectorID, boolean newRow) {
        super(newRow);

        this.id = id;
        this.login = new ImString();
        this.login.set(login);

        this.newPassword = new ImString();
        this.newPassword.set("Введите новый пароль");

        this.newSalt = new ImString();

        this.role = new ImString();
        this.role.set(role);

        this.collectorID = new ImInt(collectorID);
    }

    public int getID() {
        return id;
    }

    public ImString login() {
        return login;
    }

    public ImString newPassword() {
        return newPassword;
    }

    public String getNewPassword() {
        return newPassword.get();
    }

    public ImString newSalt() {
        return newSalt;
    }

    public String getNewSalt() {
        return newSalt.get();
    }

    public String getLogin() {
        return login.get();
    }

    public ImString role() {
        return role;
    }

    public String getRole() {
        return role.get();
    }

    public ImInt collectorID() {
        return collectorID;
    }

    public int getCollectorID() {
        return collectorID.get();
    }
}
