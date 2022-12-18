package me.kirillirik.manulab.main.table;

import imgui.ImGui;
import imgui.ImGuiListClipper;
import imgui.callback.ImListClipperCallback;
import imgui.flag.ImGuiTableFlags;
import me.kirillirik.database.Database;
import me.kirillirik.manulab.main.TableType;
import me.kirillirik.manulab.main.table.row.TableRow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Table <T extends TableRow> {

    private final int columnCount;
    private final TableType type;
    private final List<T> rows;

    public Table(TableType type, int columnCount) {
        super();

        this.columnCount = columnCount;
        this.type = type;
        this.rows = Collections.synchronizedList(new ArrayList<>());
    }

    protected abstract T getRow(ResultSet resultSet) throws SQLException;

    protected abstract void initTableConfig();

    protected abstract void tableRow(T row);

    protected abstract void saveRow(T row);

    public TableType getType() {
        return type;
    }

    public void refresh(boolean saveDirty) {
        if (saveDirty) {
            for (final T row : rows) {
                if (!row.isDirty()) {
                    continue;
                }

                saveRow(row);
            }
        }

        Database.sync().rs("select * from " + type.name().toLowerCase(), rs -> {
            rows.clear();

            while (rs.next()) {
                final T row = getRow(rs);
                rows.add(row);
            }

            return null;
        });
    }

    public void update() {
        final int flags = ImGuiTableFlags.Resizable | ImGuiTableFlags.Reorderable | ImGuiTableFlags.Hideable |
                          ImGuiTableFlags.Sortable | ImGuiTableFlags.SortMulti |
                          ImGuiTableFlags.RowBg | ImGuiTableFlags.Borders | ImGuiTableFlags.NoBordersInBody | ImGuiTableFlags.ScrollY;

        ImGui.beginChild(type.getName() + "_child", ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY() * 0.8f, false);

        ImGui.beginTable(type.getName(), columnCount, flags);

        initTableConfig();

        ImGui.tableHeadersRow();

        ImGuiListClipper.forEach(rows.size(), new ImListClipperCallback() {
            @Override
            public void accept(int index) {
                ImGui.pushID(index);

                ImGui.tableNextRow(ImGuiTableFlags.None, 10);

                final T row = rows.get(index);
                tableRow(row);

                ImGui.popID();
            }
        });

        ImGui.endTable();

        ImGui.endChild();

        if (ImGui.button("Обновить")) {
            refresh(false);
        }

        ImGui.sameLine();
        if (ImGui.button("Сохранить")) {
            refresh(true);
        }
    }
}
