package me.kirillirik.manulab.auth;


import java.util.HashSet;
import java.util.Set;

public final class Role {

    public static final RoleBuilder ADMIN = new RoleBuilder("ADMIN");
    public static final RoleBuilder DIRECTOR = new RoleBuilder("DIRECTOR");
    public static final RoleBuilder MANAGER = new RoleBuilder("MANAGER");
    public static final RoleBuilder COLLECTOR = new RoleBuilder("COLLECTOR");
    public static final RoleBuilder USER = new RoleBuilder("USER");


    private final String name;
    private final Set<Permission> permissions;

    private Role(String name, Permission... permissions) {
        this.name = name;
        this.permissions = new HashSet<>(Set.of(permissions));
    }

    public String getName() {
        return name;
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

    public record RoleBuilder(String name) {

        public Role get() {
            return new Role(name);
        }

        public Role addPermissions(Permission... permission) {
            return new Role(name, permission);
        }
    }
}
