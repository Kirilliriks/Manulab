package me.kirillirik;

public final class User {

    private final String login;
    private final String role;
    private final int collector;

    public User(String login, String role) {
        this(login, role, -1);
    }

    public User(String login, String role, int collector) {
        this.login = login;
        this.role = role;
        this.collector = collector;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }

    public int getCollector() {
        return collector;
    }
}
