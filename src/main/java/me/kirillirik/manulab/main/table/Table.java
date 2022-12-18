package me.kirillirik.manulab.main.table;

import imgui.ImGui;
import imgui.ImGuiListClipper;
import imgui.callback.ImListClipperCallback;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import me.kirillirik.database.Database;
import me.kirillirik.manulab.main.TableType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class Table <T extends TableRow> {

    protected final int columnCount;
    protected final TableType type;
    protected final List<T> rows;
    protected final Set<T> selectedRows;
    protected boolean dirty;

    public Table(TableType type, int columnCount) {
        super();

        this.columnCount = columnCount;
        this.type = type;
        this.rows = Collections.synchronizedList(new ArrayList<>());
        this.selectedRows = new HashSet<>();
    }

    protected abstract T newRow();
    protected abstract T getRow(ResultSet rs) throws SQLException;
    protected abstract void removeRow(T row);

    protected abstract void initTableConfig();

    protected abstract void addRow(int index, T row);

    protected abstract void updateRow(T row);

    protected abstract void insertRow(T row);

    protected void addNewRow() {
        rows.add(newRow());

        dirty = true;
    }

    public void removeSelectedRows() {
        for (final T row  : selectedRows) {
            removeRow(row);
        }

        refresh(true);
    }

    public void refresh(boolean saveDirty) {
        if (saveDirty && dirty) {
            for (final T row : rows) {
                if (!row.isDirty()) {
                    continue;
                }

                if (row.isNewRow()) {
                    insertRow(row);
                    continue;
                }

                updateRow(row);
            }
        }

        Database.async().rs("select * from \"" + type.name().toLowerCase() + "\"", rs -> {
            rows.clear();

            while (rs.next()) {
                final T row = getRow(rs);
                rows.add(row);
            }

            return null;
        });

        dirty = false;
    }

    public void update() {
        final int flags = ImGuiTableFlags.Resizable | ImGuiTableFlags.Reorderable | ImGuiTableFlags.Hideable |
                          ImGuiTableFlags.Sortable | ImGuiTableFlags.SortMulti |
                          ImGuiTableFlags.RowBg | ImGuiTableFlags.Borders | ImGuiTableFlags.NoBordersInBody | ImGuiTableFlags.ScrollY;

        ImGui.beginChild(type.getName() + "_child", ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY() * 0.8f, false);

        ImGui.beginTable(type.getName(), columnCount + 1, flags);

        ImGui.tableSetupColumn("Index", ImGuiTableColumnFlags.NoHide);
        initTableConfig();

        ImGui.tableHeadersRow();

        ImGuiListClipper.forEach(rows.size(), new ImListClipperCallback() {
            @Override
            public void accept(int index) {
                final T row = rows.get(index);

                ImGui.pushID(index);

                ImGui.tableNextRow(ImGuiTableFlags.None, 10);

                ImGui.tableSetColumnIndex(0);

                final boolean selected = selectedRows.contains(row);
                ImGui.pushStyleVar(ImGuiStyleVar.SelectableTextAlign, 0.5f, 0.5f);
                if (ImGui.selectable(String.valueOf(index), selected)) {
                    if (selected) {
                        selectedRows.remove(row);
                    } else {
                        selectedRows.add(row);
                    }
                }
                ImGui.popStyleVar();

                if (ImGui.beginPopupContextItem()) {
                    if (ImGui.selectable("Добавить строку")) {
                        addNewRow();
                    }

                    if (ImGui.selectable("Удалить строку")) {
                        removeRow(row);

                        refresh(true);
                    }

                    if (selectedRows.size() != 0) {
                        if (ImGui.selectable("Удалить выбранные строки")) {
                            removeSelectedRows();
                        }
                    }

                    if (ImGui.selectable("Обновить")) {
                        refresh(false);
                    }

                    if (ImGui.selectable("Сохранить")) {
                        refresh(true);
                    }

                    ImGui.endPopup();
                }

                addRow(index, row);

                ImGui.popID();
            }
        });

        ImGui.endTable();

        ImGui.endChild();

        if (ImGui.button("Добавить строку")) {
            addNewRow();
        }

        ImGui.sameLine();
        if (ImGui.button("Удалить выбранные строки")) {
            removeSelectedRows();
        }

        if (ImGui.button("Обновить")) {
            refresh(false);
        }

        ImGui.sameLine();
        if (ImGui.button("Сохранить")) {
            refresh(true);
        }
    }

    public TableType getType() {
        return type;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void dirty() {
        dirty = true;
    }
}
