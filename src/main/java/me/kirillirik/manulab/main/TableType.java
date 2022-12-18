package me.kirillirik.manulab.main;

import me.kirillirik.manulab.auth.Permission;
import me.kirillirik.manulab.auth.Role;
import me.kirillirik.manulab.main.table.Table;
import me.kirillirik.manulab.main.table.assembly.AssemblyTable;
import me.kirillirik.manulab.main.table.collector.CollectorTable;
import me.kirillirik.manulab.main.table.manufactory.ManufactoryTable;
import me.kirillirik.manulab.main.table.product.ProductTable;
import me.kirillirik.manulab.main.table.user.UserTable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum TableType {
    MANUFACTORY("Цехи",
            ManufactoryTable::new,
            Role.ADMIN.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.DIRECTOR.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.MANAGER.addPermissions(Permission.VIEW),
            Role.COLLECTOR.addPermissions(Permission.VIEW)
    ),
    COLLECTOR("Сборщики",
            CollectorTable::new,
            Role.ADMIN.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.DIRECTOR.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.MANAGER.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.COLLECTOR.addPermissions(Permission.VIEW)
    ),
    ASSEMBLY("Сборки",
            AssemblyTable::new,
            Role.ADMIN.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.DIRECTOR.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.MANAGER.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.COLLECTOR.addPermissions(Permission.EDIT, Permission.VIEW)
    ),
    PRODUCT("Изделия",
            ProductTable::new,
            Role.ADMIN.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.DIRECTOR.addPermissions(Permission.EDIT_ALL, Permission.VIEW),
            Role.MANAGER.addPermissions(Permission.VIEW),
            Role.COLLECTOR.addPermissions(Permission.VIEW)
    ),
    USER("Пользователи",
            UserTable::new,
            Role.ADMIN.addPermissions(Permission.EDIT_ALL, Permission.VIEW)
    );

    private final String name;
    private final Supplier<? extends Table<?>> table;
    private final Map<String, Role> roles;

    TableType(String name, Supplier<? extends Table<?>> table, Role... roles) {
        this.name = name;
        this.table = table;
        this.roles = new HashMap<>();

        for (final Role role : roles) {
            this.roles.put(role.getName(), role);
        }
    }

    public String getName() {
        return name;
    }

    public Table<?> getTable() {
        return table.get();
    }

    public Role getRole(Role.RoleBuilder role) {
        return roles.get(role.name());
    }
}
