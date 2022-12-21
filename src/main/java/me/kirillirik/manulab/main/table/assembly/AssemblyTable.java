package me.kirillirik.manulab.main.table.assembly;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiInputTextFlags;
import me.kirillirik.User;
import me.kirillirik.database.Database;
import me.kirillirik.manulab.auth.Auth;
import me.kirillirik.manulab.auth.Permission;
import me.kirillirik.manulab.auth.Role;
import me.kirillirik.manulab.main.TableType;
import me.kirillirik.manulab.main.table.Table;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class AssemblyTable extends Table<AssemblyRow> {

    public AssemblyTable() {
        super(TableType.ASSEMBLY, 5);
    }

    @Override
    protected void sort() {
        switch (columnSort.left) {
            case 1 -> rows.sort((o1, o2) -> {
                if (columnSort.right) {
                    return o1.getID() - o2.getID();
                } else {
                    return o2.getID() - o1.getID();
                }
            });
            case 2 -> rows.sort((o1, o2) -> {
                if (columnSort.right) {
                    return o1.getAssemblyDate().compareTo(o2.getAssemblyDate());
                } else {
                    return o2.getAssemblyDate().compareTo(o1.getAssemblyDate());
                }
            });
            case 3 -> rows.sort((o1, o2) -> {
                if (columnSort.right) {
                    return o1.getAmount() - o2.getAmount();
                } else {
                    return o2.getAmount() - o1.getAmount();
                }
            });
            case 4 -> rows.sort((o1, o2) -> {
                if (columnSort.right) {
                    return o1.getCollectorID() - o2.getCollectorID();
                } else {
                    return o2.getCollectorID() - o1.getCollectorID();
                }
            });
            case 5 -> rows.sort((o1, o2) -> {
                if (columnSort.right) {
                    return o1.getProductID() - o2.getProductID();
                } else {
                    return o2.getProductID() - o1.getProductID();
                }
            });
        }
    }

    @Override
    protected AssemblyRow newRow() {
        return new AssemblyRow(-1, "Введите дату", -1, Auth.user().getCollectorID(), -1, true);
    }

    @Override
    protected AssemblyRow getRow(ResultSet rs) throws SQLException {
        return new AssemblyRow(rs.getInt("id"),
                               rs.getDate("assembly_date").toString(),
                               rs.getInt("amount"),
                               rs.getInt("collector_id"),
                               rs.getInt("product_id"), false);
    }

    @Override
    protected void removeRow(AssemblyRow row) {
        Database.sync().update("delete from assembly where id = ?", row.getID());
    }

    @Override
    protected void initTableConfig() {
        ImGui.tableSetupColumn("ID");
        ImGui.tableSetupColumn("Дата");
        ImGui.tableSetupColumn("Количество");
        ImGui.tableSetupColumn("ID сборщика");
        ImGui.tableSetupColumn("ID изделия");
    }

    @Override
    protected void displayRow(int index, boolean canEdit, AssemblyRow row) {
        if (!canEdit && Auth.user().getCollectorID() == row.getCollectorID()) {
            canEdit = true;
        }

        super.displayRow(index, canEdit, row);
    }

    @Override
    protected void displayRowData(int index, boolean canEdit, AssemblyRow row) {
        ImGui.tableSetColumnIndex(1);
        ImGui.text(String.valueOf(row.getID()));

        if (!canEdit) {
            ImGui.pushStyleColor(ImGuiCol.Text, 0, 0, 0, 255);
        }

        ImGui.tableSetColumnIndex(2);
        if (ImGui.inputText("##assembly_date",  row.assemblyDate(), canEdit ? ImGuiInputTextFlags.None : ImGuiInputTextFlags.ReadOnly)) {
            row.dirty();

            dirty();
        }

        ImGui.tableSetColumnIndex(3);
        if (ImGui.inputInt("##amount",  row.amount(), 1, 0, canEdit ? ImGuiInputTextFlags.None : ImGuiInputTextFlags.ReadOnly)) {
            row.dirty();

            dirty();
        }


        final User user = Auth.user();
        final Role userRole = user.getRole();
        final Role tableRole = type.getRole(userRole.getTypeName());
        final boolean cantEdit = tableRole != null &&  tableRole.getPermissions().contains(Permission.EDIT) && user.getCollectorID() == row.getCollectorID();

        ImGui.tableSetColumnIndex(4);
        if (ImGui.inputInt("##collector_id", row.collectorID(), 1, 0, canEdit && !cantEdit ? ImGuiInputTextFlags.None : ImGuiInputTextFlags.ReadOnly)) {
            row.dirty();

            dirty();
        }

        ImGui.tableSetColumnIndex(5);
        if (ImGui.inputInt("##product_id",  row.productID(), 1, 0, canEdit ? ImGuiInputTextFlags.None : ImGuiInputTextFlags.ReadOnly)) {
            row.dirty();

            dirty();
        }

        if (!canEdit) {
            ImGui.popStyleColor();
        }
    }

    @Override
    protected void updateRow(AssemblyRow row) {
        Database.sync().update("update assembly set assembly_date = ?, amount = ?, collector_id = ?, product_id = ? where id = ?",
                Date.valueOf(row.getAssemblyDate()), row.getAmount(), row.getCollectorID(), row.getProductID(), row.getID());
    }

    @Override
    protected void insertRow(AssemblyRow row) {
        Database.sync().insert("insert into assembly(assembly_date, amount, collector_id, product_id) values(?, ?, ?, ?)", rs -> null,
                Date.valueOf(row.getAssemblyDate()), row.getAmount(), row.getCollectorID(), row.getProductID());
    }
}
