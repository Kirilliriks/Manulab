package me.kirillirik.manulab.main.table.collector;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import me.kirillirik.database.Database;
import me.kirillirik.manulab.main.TableType;
import me.kirillirik.manulab.main.table.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CollectorTable extends Table<CollectorRow> {

    public CollectorTable() {
        super(TableType.COLLECTOR, 4);
    }

    @Override
    protected CollectorRow newRow() {
        return new CollectorRow(-1, "Введите фамилию", "Введите имя", -1, true);
    }

    @Override
    protected CollectorRow getRow(ResultSet rs) throws SQLException {
        return new CollectorRow(rs.getInt("id"),
                                rs.getString("second_name"),
                                rs.getString("first_name"),
                                rs.getInt("manufactory_id"), false);
    }

    @Override
    protected void removeRow(CollectorRow row) {
        Database.sync().update("delete from collector where id = ?", row.getID());
    }

    @Override
    protected void initTableConfig() {
        ImGui.tableSetupColumn("ID");
        ImGui.tableSetupColumn("Фамилия");
        ImGui.tableSetupColumn("Имя");
        ImGui.tableSetupColumn("ID цеха");
    }

    @Override
    protected void displayRowData(int index, boolean canEdit, CollectorRow row) {
        ImGui.tableSetColumnIndex(1);
        ImGui.text(String.valueOf(row.getID()));

        if (!canEdit) {
            ImGui.tableSetColumnIndex(2);
            ImGui.text(row.getSecondName());

            ImGui.tableSetColumnIndex(3);
            ImGui.text(row.getFirstName());

            ImGui.tableSetColumnIndex(4);
            ImGui.text(String.valueOf(row.getManufactoryID()));
            return;
        }

        ImGui.tableSetColumnIndex(2);
        if (ImGui.inputText("##second_name",  row.secondName(), ImGuiInputTextFlags.None)) {
            row.dirty();

            dirty();
        }

        ImGui.tableSetColumnIndex(3);
        if (ImGui.inputText("##first_name",  row.firstName(), ImGuiInputTextFlags.None)) {
            row.dirty();

            dirty();
        }

        ImGui.tableSetColumnIndex(4);
        if (ImGui.inputInt("##manufactory_id",  row.manufactoryID(), 1, 0, ImGuiInputTextFlags.None)) {
            row.dirty();

            dirty();
        }
    }

    @Override
    protected void updateRow(CollectorRow row) {
        Database.sync().update("update collector set second_name = ?, first_name = ?, manufactory_id = ? where id = ?",
                row.getSecondName(), row.getFirstName(), row.getManufactoryID(), row.getID());
    }

    @Override
    protected void insertRow(CollectorRow row) {
        Database.sync().insert("insert into collector(second_name, first_name, manufactory_id) values(?, ?, ?)", rs -> null,
                row.getSecondName(), row.getFirstName(), row.getManufactoryID());
    }
}
