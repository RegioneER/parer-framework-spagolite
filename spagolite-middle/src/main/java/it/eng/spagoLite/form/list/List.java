package it.eng.spagoLite.form.list;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.table.BaseTable;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseElements;
import it.eng.spagoLite.form.buttonList.ButtonList;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.Fields;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.fields.impl.CheckBox;
import it.eng.spagoLite.message.Message;
import it.eng.spagoLite.message.MessageBox;
import it.eng.spagoLite.model.rowsmandrupper.BaseRowSmandrupper;
import it.eng.spagoLite.model.rowsmandrupper.RowSmandrupperInterface;
import it.eng.spagoLite.util.Casting.Casting;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;

public class List<T extends SingleValueField<?>> extends BaseElements<T> {

    private static final long serialVersionUID = 1L;

    private Status status;
    private boolean hidden;
    private String title;
    private RowSmandrupperInterface rowSmandrupper;
    private boolean hideDetailButton;
    private boolean hideUpdateButton;
    private boolean hideDeleteButton;
    private boolean hideInsertButton;
    private boolean editable; // si riferisce alla riga modificabile
    private boolean masterList;
    private boolean readOnly; // si riferisce alle operazioni possibili sulla lista
    private String visibilityProperty;
    private Boolean filterValidRecords;
    private String excelFileName;
    private Fields genericFields;

    private ButtonList buttonList;

    private BaseTableInterface<?> table;

    public List(Component parent, String name, String description) {
        this(parent, name, description, description);
    }

    public List(Component parent, String name, String description, String title) {
        this(parent, name, description, Status.view, title, null, false);
    }

    public List(Component parent, String name, String description, Status mode, String title,
            BaseTableInterface<?> table, boolean hidden) {
        this(parent, name, description, mode, title, table, hidden, false, false, false, false);
    }

    public List(Component parent, String name, String description, Status status, String title,
            BaseTableInterface<?> table, boolean hidden, boolean hideDetailButton, boolean hideUpdateButton,
            boolean hideDeleteButton, boolean hideInsertButton) {
        this(parent, name, description, status, title, table, hidden, hideDetailButton, hideUpdateButton,
                hideDeleteButton, hideInsertButton, false, null);
    }

    public List(Component parent, String name, String description, Status status, String title,
            BaseTableInterface<?> table, boolean hidden, boolean hideDetailButton, boolean hideUpdateButton,
            boolean hideDeleteButton, boolean hideInsertButton, boolean editable) {
        this(parent, name, description, status, title, table, hidden, hideDetailButton, hideUpdateButton,
                hideDeleteButton, hideInsertButton, editable, null);
    }

    public List(Component parent, String name, String description, Status status, String title,
            BaseTableInterface<?> table, boolean hidden, boolean hideDetailButton, boolean hideUpdateButton,
            boolean hideDeleteButton, boolean hideInsertButton, String visibilityProperty) {
        this(parent, name, description, status, title, table, hidden, hideDetailButton, hideUpdateButton,
                hideDeleteButton, hideInsertButton, false, visibilityProperty, null, null);
    }

    public List(Component parent, String name, String description, Status status, String title,
            BaseTableInterface<?> table, boolean hidden, boolean hideDetailButton, boolean hideUpdateButton,
            boolean hideDeleteButton, boolean hideInsertButton, boolean editable, String visibilityProperty) {
        this(parent, name, description, status, title, table, hidden, hideDetailButton, hideUpdateButton,
                hideDeleteButton, hideInsertButton, editable, visibilityProperty, null, null);
    }

    public List(Component parent, String name, String description, Status status, String title,
            BaseTableInterface<?> table, boolean hidden, boolean hideDetailButton, boolean hideUpdateButton,
            boolean hideDeleteButton, boolean hideInsertButton, boolean editable, String visibilityProperty,
            Boolean filterValidRecords, String excelFileName) {
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
        this.editable = editable;
        this.visibilityProperty = visibilityProperty;
        this.filterValidRecords = filterValidRecords;
        this.excelFileName = excelFileName;
    }

    public BaseTableInterface<?> getTable() {
        return table;
    }

    /**
     * Restituisce un table bean del tipo passato in input arricchito dalle informazioni del table bean referenziato
     * dalla lista
     *
     * @param inTable
     *            interfaccia table
     * 
     * @return implementazione dell'interfaccia
     */
    public BaseTableInterface<?> toTable(BaseTableInterface<?> inTable) {
        if (table != null) {
            inTable.load(table);
            inTable.setCurrentRowIndex(table.getCurrentRowIndex());
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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void loadFromRow(BaseRowInterface row) throws EMFError {
        for (SingleValueField<?> field : this) {
            field.format(row);
        }
    }

    public void writeExcel(OutputStream outputStream) throws EMFError {
        try {
            new ListExcelWriter(this).write(outputStream);
        } catch (Exception e) {
            throw new EMFError(EMFError.WARNING, "Errore nella scrittura su excel", e);
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
     * @param rowIndex
     *            numero riga
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

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void clear() {
        setTable(null);
    }

    public void setUserOperations(boolean canSelect, boolean canUpdate, boolean canInsert, boolean canDelete) {
        setHideDetailButton(!canSelect);
        setHideUpdateButton(!canUpdate);
        setHideInsertButton(!canInsert);
        setHideDeleteButton(!canDelete);
    }

    public void post(HttpServletRequest servletRequest) throws EMFError {
        // INDICI: CURRENTPAGE*PAGESIZE -> CURRENTPAGE*PAGESIZE + PAGESIZE
        int i = 0;
        if (getTable() != null) {
            int startIndex = (getTable().getCurrentPageIndex() - 1) * getTable().getPageSize();
            int endIndex = ((getTable().getCurrentPageIndex() - 1) * getTable().getPageSize())
                    + getTable().getPageSize();
            for (int index = startIndex; index < endIndex; index++) {
                if (index < getTable().size()) {
                    BaseRowInterface row = getTable().getRow(index);
                    for (SingleValueField<?> field : this) {
                        if (!field.isReadonly() && field.isEditMode()) {
                            final String name = field.getName();
                            String[] param = servletRequest.getParameterValues(name);
                            if (field instanceof CheckBox || field instanceof CheckBox<?>) {
                                row.setOldObject(name, row.getObject(name));
                                row.setObject(name,
                                        ArrayUtils.contains(param, "" + index)
                                                ? Casting.parse(CheckBox.valueChecked, field.getType())
                                                : Casting.parse(CheckBox.valueUnchecked, field.getType()));
                            } else {
                                row.setOldObject(name, row.getObject(name));
                                row.setObject(name, param[i]);
                            }
                        }
                    }
                    i++;
                }
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

    public boolean postAndValidate(HttpServletRequest servletRequest, MessageBox messageBox) throws EMFError {
        post(servletRequest);
        return validate(messageBox);
    }

    /**
     * Compila il row bean con il contenuto della riga corrente
     *
     * @throws EMFError
     *             eccezione generica
     */
    public void copyToBean() throws EMFError {
        if (table != null && table.size() > 0) {
            copyToBean(table.getCurrentRow());
        }

    }

    /**
     * Compila il row bean con il contenuto del form
     *
     * @param row
     *            oggetto bean
     * 
     * @throws EMFError
     *             eccezione generica
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

    public String getVisibilityProperty() {
        return visibilityProperty;
    }

    public void setVisibilityProperty(String visibilityProperty) {
        this.visibilityProperty = visibilityProperty;
    }

    public Boolean isFilterValidRecords() {
        return filterValidRecords;
    }

    public void setFilterValidRecords(Boolean filterValidRecords) {
        this.filterValidRecords = filterValidRecords;
    }

    public String getExcelFileName() {
        return excelFileName;
    }

    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }

    public Fields getGenericFields() {
        return genericFields;
    }

    public void setGenericFields(Fields genericFields) {
        this.genericFields = genericFields;
    }

}
