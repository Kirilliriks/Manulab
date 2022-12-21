package me.kirillirik.manulab.main.table.product;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import me.kirillirik.database.Database;
import me.kirillirik.manulab.main.TableType;
import me.kirillirik.manulab.main.table.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ProductTable extends Table<ProductRow> {

    public ProductTable() {
        super(TableType.PRODUCT, 2);
    }

    @Override
    protected ProductRow newRow() {
        return new ProductRow(-1, "Введите тип", true);
    }

    @Override
    protected ProductRow getRow(ResultSet rs) throws SQLException {
        return new ProductRow(rs.getInt("id"), rs.getString("type"), false);
    }

    @Override
    protected void removeRow(ProductRow row) {
        Database.sync().update("delete from product where id = ?", row.getID());
    }

    @Override
    protected void initTableConfig() {
        ImGui.tableSetupColumn("ID");
        ImGui.tableSetupColumn("Тип");
    }

    @Override
    protected void displayRowData(int index, boolean canEdit, ProductRow row) {
        ImGui.tableSetColumnIndex(1);
        ImGui.text(String.valueOf(row.getID()));

        if (!canEdit) {
            ImGui.tableSetColumnIndex(2);
            ImGui.text(row.getType());
            return;
        }

        ImGui.tableSetColumnIndex(2);
        ImGui.pushItemWidth(ImGui.getContentRegionAvailX());
        if (ImGui.inputText("##type",  row.type(), ImGuiInputTextFlags.Password)) {
            row.dirty();

            dirty();
        }
    }

    @Override
    protected void updateRow(ProductRow row) {
        Database.sync().update("update product set type = ? where id = ?", row.getType(), row.getID());
    }

    @Override
    protected void insertRow(ProductRow row) {
        Database.sync().insert("insert into product(type) values(?)", rs -> null, row.getType());
    }
}
