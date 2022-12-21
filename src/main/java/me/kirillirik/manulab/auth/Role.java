package me.kirillirik.manulab.auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Role {

    public static final List<Type> VALUES = new ArrayList<>();

    public static final Type ADMIN = new Type(1, "ADMIN");
    public static final Type DIRECTOR = new Type(2, "DIRECTOR");
    public static final Type MANAGER = new Type(3, "MANAGER");
    public static final Type COLLECTOR = new Type(4, "COLLECTOR");
    public static final Type USER = new Type(5, "USER");

    private final String type;
    private final Set<Permission> permissions;

    private Role(String name, Permission... permissions) {
        this.type = name;
        this.permissions = new HashSet<>(Set.of(permissions));
    }

    public String getTypeName() {
        return type;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Role addPermissions(Permission... permission) {
        permissions.addAll(Set.of(permission));
        return this;
    }

    public static Role of(String name) {
        return type(name).get();
    }

    public static Type type(String name) {
        return switch (name.toUpperCase()) {
            case "ADMIN" -> ADMIN;
            case "DIRECTOR" -> DIRECTOR;
            case "MANAGER" -> MANAGER;
            case "COLLECTOR" -> COLLECTOR;
            default -> USER;
        };
    }

    public record Type(int priority, String name) {

        public Type(int priority, String name) {
            this.priority = priority;
            this.name = name;

            VALUES.add(this);
        }

        public Role get() {
            return new Role(name);
        }

        public Role addPermissions(Permission... permission) {
            return new Role(name, permission);
        }
    }
}
