package me.kirillirik.manulab.auth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Role {

    public static final List<Type> VALUES = new ArrayList<>();

    public static final Type ADMIN = new Type("ADMIN");
    public static final Type DIRECTOR = new Type("DIRECTOR");
    public static final Type MANAGER = new Type("MANAGER");
    public static final Type COLLECTOR = new Type("COLLECTOR");
    public static final Type USER = new Type("USER");

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
        return switch (name.toUpperCase()) {
            case "ADMIN" -> ADMIN.get();
            case "DIRECTOR" -> DIRECTOR.get();
            case "MANAGER" -> MANAGER.get();
            case "COLLECTOR" -> COLLECTOR.get();
            default -> USER.get();
        };
    }

    public record Type(String name) {

        public Type(String name) {
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
