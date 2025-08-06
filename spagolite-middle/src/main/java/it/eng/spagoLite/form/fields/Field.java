/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoLite.form.fields;

import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseComponent;
import it.eng.spagoLite.message.Message;
import it.eng.spagoLite.xmlbean.form.Field.Type;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;

import javax.servlet.http.HttpServletRequest;

public abstract class Field extends BaseComponent implements Cloneable {

    private static final long serialVersionUID = 1L;

    private String alias;
    private Type.Enum type;
    private String format;
    private boolean required;
    private boolean hidden;
    private boolean readonly;
    private boolean trigger;

    private boolean editMode;

    public Field(Component parent, String name, String description, Enum type) {
	this(parent, name, description, null, type, null, false, false, false, false);
    }

    public Field(Component parent, String name, String description, String alias, Enum type,
	    String format, boolean required, boolean hidden, boolean readonly, boolean trigger) {
	super(parent, name, description);
	this.alias = alias;
	this.type = type;
	this.format = format;
	this.required = required;
	this.hidden = hidden;
	this.readonly = readonly;
	this.trigger = trigger;
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

    public boolean isHidden() {
	return hidden;
    }

    public void setHidden(boolean hidden) {
	this.hidden = hidden;
    }

    public boolean isReadonly() {
	return readonly;
    }

    public void setReadonly(boolean readonly) {
	this.readonly = readonly;
    }

    public Type.Enum getType() {
	return type;
    }

    public void setType(Type.Enum type) {
	this.type = type;
    }

    public String getFormat() {
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }

    public boolean isRequired() {
	return required;
    }

    public void setRequired(boolean required) {
	this.required = required;
    }

    public boolean isEditMode() {
	return editMode;
    }

    public boolean isViewMode() {
	return !editMode;
    }

    public void setEditMode() {
	this.editMode = true;
    }

    public void setViewMode() {
	this.editMode = false;
    }

    public abstract void post(HttpServletRequest servletRequest);

    public abstract boolean check();

    public abstract Message validate();

    public boolean isTrigger() {
	return trigger;
    }

    public void setTrigger(boolean trigger) {
	this.trigger = trigger;
    }

    public void reset() {
	clear();
    }

    public abstract void clear();

    public Field clone() {
	try {
	    return (Field) super.clone();
	} catch (CloneNotSupportedException e) {
	    e.printStackTrace();
	    return null;
	}
    }

}
