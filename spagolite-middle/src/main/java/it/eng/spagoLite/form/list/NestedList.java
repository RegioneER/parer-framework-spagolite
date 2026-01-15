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

package it.eng.spagoLite.form.list;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.table.BaseTable;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseElements;
import it.eng.spagoLite.form.buttonList.ButtonList;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.message.Message;
import it.eng.spagoLite.message.MessageBox;
import it.eng.spagoLite.model.rowsmandrupper.BaseRowSmandrupper;
import it.eng.spagoLite.model.rowsmandrupper.RowSmandrupperInterface;

import javax.servlet.http.HttpServletRequest;

public class NestedList<T extends SingleValueField<?>> extends BaseElements<T> {

    private static final long serialVersionUID = 1L;

    private Status status;
    private boolean hidden;
    private String title;
    private RowSmandrupperInterface rowSmandrupper;
    private boolean hideDetailButton;
    private boolean hideUpdateButton;
    private boolean hideDeleteButton;
    private boolean hideInsertButton;
    private boolean masterList;

    private ButtonList buttonList;

    private BaseTableInterface<?> table;

    public NestedList(Component parent, String name, String description) {
        this(parent, name, description, description);
    }

    public NestedList(Component parent, String name, String description, String title) {
        this(parent, name, description, Status.view, title, null, false);
    }

    public NestedList(Component parent, String name, String description, Status mode, String title,
            BaseTableInterface<?> table, boolean hidden) {
        this(parent, name, description, mode, title, table, hidden, false, false, false, false);
    }

    public NestedList(Component parent, String name, String description, Status status,
            String title, BaseTableInterface<?> table, boolean hidden, boolean hideDetailButton,
            boolean hideUpdateButton, boolean hideDeleteButton, boolean hideInsertButton) {
        this(parent, name, description, status, title, table, hidden, hideDetailButton,
                hideUpdateButton, hideDeleteButton, hideInsertButton, false);
    }

    public NestedList(Component parent, String name, String description, Status status,
            String title, BaseTableInterface<?> table, boolean hidden, boolean hideDetailButton,
            boolean hideUpdateButton, boolean hideDeleteButton, boolean hideInsertButton,
            boolean editable) {
        super(parent, name, description);
        this.hideDetailButton = hideDetailButton;
        this.hideUpdateButton = hideUpdateButton;
        this.hideDeleteButton = hideDeleteButton;
        this.hideInsertButton = hideInsertButton;
        this.status = status;
        this.title = title;
        this.hidden = hidden;
        if (table != null) {
            this.table = table;
        } else {
            table = new BaseTable();
        }
        rowSmandrupper = new BaseRowSmandrupper();
    }

    public BaseTableInterface<?> getTable() {
        return table;
    }

    /**
     * Restituisce un table bean del tipo passato in input arricchito dalle informazioni del table
     * bean referenziato dalla lista
     *
     * @param inTable interfaccia table base
     *
     * @return implementazione interfaccia
     */
    public BaseTableInterface<?> toTable(BaseTableInterface<?> inTable) {
        if (table != null) {
            inTable.load(table);
        }
        return inTable;
    }

    public void setTable(BaseTableInterface<?> table) {
        this.table = table;
    }

    public RowSmandrupperInterface getRowSmandrupper() {
        return rowSmandrupper;
    }

    public void setRowSmandrupper(RowSmandrupperInterface rowSmandrupper) {
        this.rowSmandrupper = rowSmandrupper;
    }

    public void loadFromRow(BaseRowInterface row) throws EMFError {
        for (SingleValueField<?> field : this) {
            field.format(row);
        }
    }

    public void add(BaseRowInterface row) {
        getTable().add(row);
    }

    public boolean isMasterList() {
        return masterList;
    }

    public void setMasterList(boolean masterList) {
        this.masterList = masterList;
    }

    /**
     * Removes current row from the list
     *
     * @return BaseRowInterface
     */
    public BaseRowInterface remove() {
        if (getTable() != null) {
            return getTable().remove();
        } else {
            return null;
        }
    }

    /**
     * Removes row from the list
     *
     * @param rowIndex numero riga
     *
     * @return BaseRowInterface
     */
    public BaseRowInterface remove(int rowIndex) {
        if (getTable() != null) {
            return getTable().remove(rowIndex);
        } else {
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status mode) {
        this.status = mode;
    }

    public boolean isHideDetailButton() {
        return hideDetailButton;
    }

    public void setHideDetailButton(boolean hideDetailButton) {
        this.hideDetailButton = hideDetailButton;
    }

    public boolean isHideUpdateButton() {
        return hideUpdateButton;
    }

    public void setHideUpdateButton(boolean hideUpdateButton) {
        this.hideUpdateButton = hideUpdateButton;
    }

    public boolean isHideDeleteButton() {
        return hideDeleteButton;
    }

    public void setHideDeleteButton(boolean hideDeleteButton) {
        this.hideDeleteButton = hideDeleteButton;
    }

    public boolean isHideInsertButton() {
        return hideInsertButton;
    }

    public void setHideInsertButton(boolean hideInsertButton) {
        this.hideInsertButton = hideInsertButton;
    }

    public ButtonList getButtonList() {
        return buttonList;
    }

    public void setButtonList(ButtonList buttonList) {
        this.buttonList = buttonList;
    }

    public void clear() {
        setTable(null);
    }

    public void setUserOperations(boolean canSelect, boolean canUpdate, boolean canInsert,
            boolean canDelete) {
        setHideDetailButton(!canSelect);
        setHideUpdateButton(!canUpdate);
        setHideInsertButton(!canInsert);
        setHideDeleteButton(!canDelete);
    }

    public void post(HttpServletRequest servletRequest) throws EMFError {
        for (SingleValueField<?> field : this) {
            if (!field.isReadonly() && field.isEditMode()) {
                field.post(servletRequest);
            }
        }
    }

    public boolean check() {
        boolean result = true;

        for (SingleValueField<?> field : this) {
            if (!field.isReadonly() && field.isEditMode()) {
                result = result && field.check();
            }
        }

        return result;
    }

    public boolean validate(MessageBox messageBox) {

        for (SingleValueField<?> field : this) {
            if (!field.isReadonly() && field.isEditMode()) {
                Message message = field.validate();
                if (message != null) {
                    messageBox.addMessage(message);
                }
            }
        }
        if (messageBox.hasError()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean postAndValidate(HttpServletRequest servletRequest, MessageBox messageBox)
            throws EMFError {
        post(servletRequest);
        return validate(messageBox);
    }

    /**
     * Compila il row bean con il contenuto della riga corrente
     *
     * @throws EMFError eccezione generica
     */
    public void copyToBean() throws EMFError {
        if (table != null && table.size() > 0) {
            copyToBean(table.getCurrentRow());
        }

    }

    /**
     * Compila il row bean con il contenuto del form
     *
     * @param row riga oggetto BaseRowInterface
     *
     * @throws EMFError eccezione generica
     */
    public void copyToBean(BaseRowInterface row) throws EMFError {
        for (Field field : this) {
            if (field instanceof SingleValueField) {
                SingleValueField<?> singleValueField = (SingleValueField<?>) field;
                if (singleValueField.getValue() != null) {
                    row.setObject(singleValueField.getAlias(), singleValueField.parse());
                }
            }
        }
    }

    public void setViewMode() {
        for (SingleValueField<?> field : this) {
            field.setViewMode();
        }
    }

    public SingleValueField<?> getColumn(String columnName) {
        for (SingleValueField<?> field : this) {
            if (field.getName().equalsIgnoreCase(columnName)) {
                return field;
            }
        }
        return null;

    }

}
