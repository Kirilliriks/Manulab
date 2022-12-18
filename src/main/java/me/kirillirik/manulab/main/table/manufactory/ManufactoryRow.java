package me.kirillirik.manulab.main.table.manufactory;

import imgui.type.ImInt;
import imgui.type.ImString;
import me.kirillirik.manulab.main.table.row.TableRow;

public final class ManufactoryRow extends TableRow {
    private final ImInt id;
    private final String oldName;
    private final ImString name;

    public ManufactoryRow(int id, String name) {
        this.id = new ImInt(id);
        this.oldName = name;
        this.name = new ImString();
        this.name.set(name);
    }

    public ImInt ID() {
        return id;
    }

    public int getID() {
        return id.get();
    }

    public void setID(int id) {
        this.id.set(id);
    }

    public ImString name() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getOldName() {
        return oldName;
    }
}
