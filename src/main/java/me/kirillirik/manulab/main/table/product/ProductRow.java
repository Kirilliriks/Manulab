package me.kirillirik.manulab.main.table.product;

import imgui.type.ImString;
import me.kirillirik.manulab.main.table.row.TableRow;

public final class ProductRow extends TableRow {

    private final int id;
    private final ImString type;

    public ProductRow(int id, String type, boolean newRow) {
        super(newRow);

        this.id = id;
        this.type = new ImString();
        this.type.set(type);
    }

    public int getID() {
        return id;
    }

    public ImString type() {
        return type;
    }

    public String getType() {
        return type.get();
    }
}
