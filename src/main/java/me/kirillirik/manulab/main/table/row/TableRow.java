package me.kirillirik.manulab.main.table.row;

public abstract class TableRow {

    protected boolean dirty;

    public TableRow() {
        this.dirty = false;
    }

    public void dirty() {
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }
}
