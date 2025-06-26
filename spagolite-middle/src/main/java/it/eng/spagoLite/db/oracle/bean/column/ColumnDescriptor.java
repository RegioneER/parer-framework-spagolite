/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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
