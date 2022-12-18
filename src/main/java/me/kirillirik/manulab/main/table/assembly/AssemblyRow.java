package me.kirillirik.manulab.main.table.assembly;

import imgui.type.ImInt;
import imgui.type.ImString;
import me.kirillirik.manulab.main.table.TableRow;

public final class AssemblyRow extends TableRow {

    private final int id;
    private final ImString assemblyDate;
    private final ImInt amount;
    private final ImInt collectorID;
    private final ImInt productID;

    public AssemblyRow(int id, String assemblyDate, int amount, int collectorID, int productID, boolean newRow) {
        super(newRow);

        this.id = id;
        this.assemblyDate = new ImString();
        this.assemblyDate.set(assemblyDate);

        this.amount = new ImInt(amount);
        this.collectorID = new ImInt(collectorID);
        this.productID = new ImInt(productID);
    }

    public int getID() {
        return id;
    }

    public ImString assemblyDate() {
        return assemblyDate;
    }

    public String getAssemblyDate() {
        return assemblyDate.get();
    }

    public ImInt amount() {
        return amount;
    }

    public int getAmount() {
        return amount.get();
    }

    public ImInt collectorID() {
        return collectorID;
    }

    public int getCollectorID() {
        return collectorID.get();
    }

    public ImInt productID() {
        return productID;
    }

    public int getProductID() {
        return productID.get();
    }
}
