package me.kirillirik;

import me.kirillirik.manulab.auth.Role;

public final class User {

    private final int id;

    private String login;
    private Role role;
    private int collectorID;
    private boolean needUpdate;

    public User(int id, String login, String role, int collectorID) {
        this.id = id;

        this.login = login;
        this.role = Role.of(role);
        this.collectorID = collectorID;
        this.needUpdate = false;
    }

    public int getID() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = Role.of(role);
    }

    public int getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(int collectorID) {
        this.collectorID = collectorID;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }
}
