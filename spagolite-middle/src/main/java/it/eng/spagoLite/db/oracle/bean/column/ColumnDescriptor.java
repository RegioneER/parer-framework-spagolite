package it.eng.spagoLite.db.oracle.bean.column;

import it.eng.spagoLite.db.SqlTypesName;

public class ColumnDescriptor {
    private final String name;
    private final int type;
    private final int length;
    private final boolean notnull;

    public ColumnDescriptor(String name, int type) {
        this(name, type, 0, false);
    }

    public ColumnDescriptor(String name, int type, int length, boolean notnull) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.notnull = notnull;
    }

    /**
     * @return Il nome della colonna
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Il tipo della colonna
     */
    public int getType() {
        return this.type;
    }

    /**
     * @return La lunghezza massima consentita della colonna
     */
    public int getLength() {
        return this.length;
    }

    public final boolean isNotnull() {
        return this.notnull;
    }

    public String getBaseFilter() {
        return getName() + " = ?";
    }

    public String toString() {
        return this.name + ": " + toStringType();
    }

    public String toStringType() {
        return SqlTypesName.getName(this.type) + (this.length > 0 ? " (" + getLength() + ")" : "")
                + (this.notnull ? " NOT NULL" : "");
    }
}
