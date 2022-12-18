package me.kirillirik.manulab.main.table.row;

public abstract class TableRow {

    protected final boolean newRow;
    protected boolean dirty;

    public TableRow(boolean newRow) {
        this.newRow = newRow;
        this.dirty = !newRow;
    }

    public void dirty() {
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public boolean isNewRow() {
        return newRow;
    }
}
