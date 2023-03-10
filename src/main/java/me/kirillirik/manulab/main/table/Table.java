package me.kirillirik.manulab.main.table;

import imgui.ImGui;
import imgui.ImGuiListClipper;
import imgui.callback.ImListClipperCallback;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import me.kirillirik.database.Database;
import me.kirillirik.manulab.auth.Permission;
import me.kirillirik.manulab.auth.Role;
import me.kirillirik.manulab.main.TableType;
import org.apache.commons.lang3.tuple.MutablePair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class Table <T extends TableRow> {

    protected final int columnCount;
    protected final MutablePair<Integer, Boolean> columnSort;
    protected final TableType type;
    protected final List<T> rows;
    protected final Set<T> selectedRows;
    protected boolean dirty;

    public Table(TableType type, int columnCount) {
        super();

        this.columnCount = columnCount;
        this.columnSort = new MutablePair<>();
        this.columnSort.setLeft(0);
        this.columnSort.setRight(true);
        this.type = type;
        this.rows = Collections.synchronizedList(new ArrayList<>());
        this.selectedRows = new HashSet<>();
    }

    protected abstract T newRow();
    protected abstract T getRow(ResultSet rs) throws SQLException;
    protected abstract void removeRow(T row);

    protected abstract void initTableConfig();

    protected abstract void displayRowData(int index, boolean canEdit, T row);

    protected abstract void updateRow(T row);

    protected abstract void insertRow(T row);

    protected abstract void sort();

    protected String selectWhat() {
        return "*";
    }

    protected void addNewRow() {
        rows.add(newRow());

        dirty = true;
    }

    protected void displayRow(int index, boolean canEdit, T row) {
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

        if (canEdit && ImGui.beginPopupContextItem()) {
            if (ImGui.selectable("???????????????? ????????????")) {
                addNewRow();
            }

            if (ImGui.selectable("?????????????? ????????????")) {
                removeRow(row);

                refresh(true);
            }

            if (selectedRows.size() != 0) {
                if (ImGui.selectable("?????????????? ?????????????????? ????????????")) {
                    removeSelectedRows();
                }
            }

            if (ImGui.selectable("????????????????")) {
                refresh(false);
            }

            if (ImGui.selectable("??????????????????")) {
                refresh(true);
            }

            ImGui.endPopup();
        }

        displayRowData(index, canEdit, row);

        ImGui.popID();
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
                    try {
                        insertRow(row);
                    } catch (Throwable ignored) { }

                    continue;
                }

                try {
                    updateRow(row);
                } catch (Throwable ignored) { }
            }
        }

        Database.async().rs("select " + selectWhat() + " from \"" + type.name().toLowerCase() + "\"", rs -> {
            rows.clear();

            while (rs.next()) {
                final T row = getRow(rs);
                rows.add(row);
            }

            dirty = false;
            return null;
        });
    }

    public void update(Role role) {
        final Role equivalentRole =  type.getRole(role.getTypeName());
        final boolean canEditAll = equivalentRole.getPermissions().contains(Permission.EDIT_ALL);
        final boolean canEdit = equivalentRole.getPermissions().contains(Permission.EDIT);

        final int flags = ImGuiTableFlags.Resizable | ImGuiTableFlags.Reorderable | ImGuiTableFlags.Hideable |
                          ImGuiTableFlags.RowBg | ImGuiTableFlags.Borders | ImGuiTableFlags.NoBordersInBody | ImGuiTableFlags.ScrollY;

        final String infoString = (dirty ? " - [???? ??????????????????] " : " - ") + (canEditAll ?  "" : (canEdit ? "[?????????????????? ????????????????????????????]" : "[???????????? ????????????????]"));
        ImGui.text("?????????????? " + type.getName() + infoString);

        ImGui.beginChild(type.getName() + "_child", ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY() * 0.8f, false);

        ImGui.beginTable(type.getName(), columnCount + 1, flags);

        ImGui.tableSetupColumn("Index", ImGuiTableColumnFlags.NoHide);
        initTableConfig();

        ImGui.tableHeadersRow();

        ImGuiListClipper.forEach(rows.size() + 1, new ImListClipperCallback() {
            @Override
            public void accept(int index) {
                if (index == 0) {
                    ImGui.pushID(index);
                    ImGui.tableNextRow(ImGuiTableFlags.None, 10);
                    for (int i = 0; i <= columnCount; i++) {
                        ImGui.tableSetColumnIndex(i);

                        if (columnSort.getLeft() == i && i != 0) {

                            boolean sortType = columnSort.getRight();
                            if (ImGui.button((sortType ? "/\\" : "\\/") + " ####" + i)) {
                                sortType = !sortType;

                                columnSort.setRight(sortType);

                                sort();
                            }
                        } else {
                            if (ImGui.button("X ####" + i)) {
                                columnSort.setLeft(i);

                                sort();
                            }
                        }
                    }

                    ImGui.popID();
                } else {
                    final T row = rows.get(index - 1);
                    displayRow(index - 1, canEditAll, row);
                }
            }
        });

        ImGui.endTable();

        ImGui.endChild();

        if (canEditAll || canEdit) {
            if (ImGui.button("???????????????? ????????????")) {
                addNewRow();
            }

            ImGui.sameLine();
            if (ImGui.button("?????????????? ?????????????????? ????????????")) {
                removeSelectedRows();
            }
        }

        if (ImGui.button("????????????????")) {
            refresh(false);
        }

        if (canEditAll || canEdit) {
            ImGui.sameLine();
            if (ImGui.button("??????????????????")) {
                refresh(true);
            }
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
