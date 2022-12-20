package me.kirillirik.manulab.main.table.assembly;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import me.kirillirik.database.Database;
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
    protected AssemblyRow newRow() {
        return new AssemblyRow(-1, "Введите дату", -1, -1, -1, true);
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
    protected void addRow(int index, boolean canEdit, AssemblyRow row) {
        ImGui.tableSetColumnIndex(1);
        ImGui.text(String.valueOf(row.getID()));

        ImGui.tableSetColumnIndex(2);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());
        if (ImGui.inputText("##assembly_date",  row.assemblyDate(), canEdit ? ImGuiInputTextFlags.None : ImGuiInputTextFlags.ReadOnly)) {
            row.dirty();

            dirty();
        }

        ImGui.tableSetColumnIndex(3);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());
        if (ImGui.inputInt("##amount",  row.amount(), canEdit ? ImGuiInputTextFlags.None : ImGuiInputTextFlags.ReadOnly)) {
            row.dirty();

            dirty();
        }

        ImGui.tableSetColumnIndex(4);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());
        if (ImGui.inputInt("##collector_id",  row.collectorID(), canEdit ? ImGuiInputTextFlags.None : ImGuiInputTextFlags.ReadOnly)) {
            row.dirty();

            dirty();
        }

        ImGui.tableSetColumnIndex(5);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());
        if (ImGui.inputInt("##product_id",  row.productID(), canEdit ? ImGuiInputTextFlags.None : ImGuiInputTextFlags.ReadOnly)) {
            row.dirty();

            dirty();
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
