package me.kirillirik.manulab.main.table.manufactory;

import imgui.type.ImString;
import me.kirillirik.manulab.main.table.row.TableRow;

public final class ManufactoryRow extends TableRow {
    private final int id;
    private final ImString name;

    public ManufactoryRow(int id, String name, boolean newRow) {
        super(newRow);

        this.id = id;
        this.name = new ImString();
        this.name.set(name);
    }

    public int getID() {
        return id;
    }

    public ImString name() {
        return name;
    }

    public String getName() {
        return name.get();
    }
}
