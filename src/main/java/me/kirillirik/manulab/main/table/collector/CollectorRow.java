package me.kirillirik.manulab.main.table.collector;

import imgui.type.ImInt;
import imgui.type.ImString;
import me.kirillirik.manulab.main.table.TableRow;

public final class CollectorRow extends TableRow {

    private final int id;
    private final ImString secondName;
    private final ImString firstName;
    private final ImInt manufactoryID;

    public CollectorRow(int id, String secondName, String firstName, int manufactoryID, boolean newRow) {
        super(newRow);

        this.id = id;

        this.secondName = new ImString();
        this.secondName.set(secondName);

        this.firstName = new ImString();
        this.firstName.set(firstName);

        this.manufactoryID = new ImInt(manufactoryID);
    }

    public int getID() {
        return id;
    }

    public ImString secondName() {
        return secondName;
    }

    public String getSecondName() {
        return secondName.get();
    }

    public ImString firstName() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public ImInt manufactoryID() {
        return manufactoryID;
    }

    public int getManufactoryID() {
        return manufactoryID.get();
    }
}
