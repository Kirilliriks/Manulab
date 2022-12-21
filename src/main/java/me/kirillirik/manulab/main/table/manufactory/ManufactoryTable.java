package me.kirillirik.manulab.main.table.manufactory;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import me.kirillirik.database.Database;
import me.kirillirik.manulab.main.TableType;
import me.kirillirik.manulab.main.table.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ManufactoryTable extends Table<ManufactoryRow> {

    public ManufactoryTable() {
        super(TableType.MANUFACTORY, 2);
    }

    @Override
    protected ManufactoryRow newRow() {
        return new ManufactoryRow(-1, "Введите имя", true);
    }

    @Override
    protected ManufactoryRow getRow(ResultSet rs) throws SQLException {
        return new ManufactoryRow(rs.getInt("id"), rs.getString("name"), false);
    }

    @Override
    protected void removeRow(ManufactoryRow row) {
        Database.sync().update("delete from manufactory where id = ?", row.getID());
    }

    @Override
    protected void initTableConfig() {
        ImGui.tableSetupColumn("ID");
        ImGui.tableSetupColumn("Название");
    }

    @Override
    protected void displayRowData(int index, boolean canEdit, ManufactoryRow row) {
        ImGui.tableSetColumnIndex(1);
        ImGui.text(String.valueOf(row.getID()));

        if (!canEdit) {
            ImGui.tableSetColumnIndex(2);
            ImGui.text(row.getName());
            return;
        }

        ImGui.tableSetColumnIndex(2);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());
        if (ImGui.inputText("##name",  row.name(), ImGuiInputTextFlags.None)) {
            row.dirty();

            dirty();
        }
    }

    @Override
    protected void updateRow(ManufactoryRow row) {
        Database.sync().update("update manufactory set name = ? where id = ?", row.getName(), row.getID());
    }

    @Override
    protected void insertRow(ManufactoryRow row) {
        Database.sync().insert("insert into manufactory(name) values(?)", rs -> null, row.getName());
    }
}
