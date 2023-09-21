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

package it.eng.spagoLite.form.tree;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseComponent;
import it.eng.spagoLite.xmlbean.form.TreeElement.Type;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class TreeField<O extends Object> extends BaseComponent implements Cloneable {

    private static final long serialVersionUID = 1L;

    private String alias;
    private Type.Enum type;
    private String value;
    private String icon;

    public TreeField(Component parent, String name, String description, Type.Enum type) {
        this(parent, name, description, null, type);
    }

    public TreeField(Component parent, String name, String description, String alias, Type.Enum type) {
        super(parent, name, description);
        this.alias = alias;
        this.type = type;
    }

    public TreeField(Component parent, String name, String description, String alias, Type.Enum type, String icon) {
        this(parent, name, description, alias, type);
        this.icon = icon;
    }

    public String getAlias() {
        if (this.alias == null || "".equals(this.alias)) {
            return getName();
        } else {
            return alias;
        }
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Type.Enum getType() {
        return type;
    }

    public void setType(Type.Enum type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public TreeField clone() {
        try {
            return (TreeField) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getValue() {
        return value;
    }

    public String getHtmlValue() throws EMFError {
        String htmlValue = JavaScript.stringToHTMLString(getValue());

        return htmlValue;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDecodedValue() throws EMFError {
        return getValue();
    }

    public String getHtmlDecodedValue() throws EMFError {
        return getHtmlValue();
    }

    @SuppressWarnings("unchecked")
    public String format(BaseRowInterface row) throws EMFError {
        return format((O) row.getObject(getAlias()));
    }

    public String format(Object value) throws EMFError {
        setValue(format(value, getType()));
        return this.value;
    }

    public String format(Object value, it.eng.spagoLite.xmlbean.form.TreeElement.Type.Enum type) {
        String valueString = "";
        if (type.equals(Type.ID) || type.equals(Type.ID_PARENT)) {
            BigDecimal integer = (BigDecimal) value;
            if (integer == null) {
                valueString = "";
            } else {
                valueString = String.valueOf(integer);
            }
        } else if (type.equals(Type.NAME)) {
            valueString = (String) value;
        }
        return valueString;

    }

    @SuppressWarnings("unchecked")
    public O parse() throws EMFError {
        return (O) parse(getValue(), getType());
    }

    private Object parse(String value, it.eng.spagoLite.xmlbean.form.TreeElement.Type.Enum type) throws EMFError {
        if (StringUtils.isNotBlank(value)) {
            if (type.equals(Type.ID) || type.equals(Type.ID_PARENT)) {
                return parseBigDecimal(value);
            } else if (type.equals(Type.NAME)) {
                return StringUtils.trim(value);
            }
        }
        return null;
    }

    private BigDecimal parseBigDecimal(String value) throws EMFError {
        if (!NumberUtils.isDigits(value) || !NumberUtils.isNumber(value)) {
            throw new EMFError(EMFError.WARNING, "Errore nella conversione di formato");
        } else {
            return NumberUtils.createBigDecimal(value);
        }
    }
}
