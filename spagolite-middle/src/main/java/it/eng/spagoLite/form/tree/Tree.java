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
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.table.BaseTable;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseElements;

public class Tree<T extends TreeField<?>> extends BaseElements<T> {

    private static final long serialVersionUID = 1L;

    private boolean hideCreateNodeButton;
    private boolean hideDeleteNodeButton;
    private boolean hideRenameNodeButton;
    private boolean useDefaultCreateNodeButton;
    private boolean useDefaultDeleteNodeButton;
    private boolean useDefaultRenameNodeButton;
    private boolean checkable;
    private boolean editable;
    private boolean draggable;
    private boolean sorted;
    private boolean activeContextMenu;
    private boolean coloredIcons;
    private String title;
    private boolean hidden;
    private String dataType = DATA_TYPES.HTML.name();

    public enum DATA_TYPES {
        HTML, JSON
    }

    private BaseTableInterface<?> table;

    public enum IconColours {
        BLACK, BLUE, RED, GREEN, YELLOW
    }

    public Tree(Component parent, String name, String description) {
        super(parent, name, description);
    }

    public Tree(Component parent, String name, String description, String title) {
        this(parent, name, description, title, null, false);
    }

    public Tree(Component parent, String name, String description, String title, BaseTableInterface<?> table,
            boolean hidden) {
        this(parent, name, description, title, table, hidden, false, true, false, true);
    }

    public Tree(Component parent, String name, String description, String title, BaseTableInterface<?> table,
            boolean hidden, boolean checkable, boolean editable, boolean draggable, boolean sorted) {
        this(parent, name, description, title, table, hidden, false, true, false, true, false, true, checkable,
                editable, draggable, sorted);
    }

    public Tree(Component parent, String name, String description, String title, BaseTableInterface<?> table,
            boolean hidden, boolean hideCreateNodeButton, boolean useDefaultCreateNodeButton,
            boolean hideDeleteNodeButton, boolean useDefaultDeleteNodeButton, boolean hideRenameNodeButton,
            boolean useDefaultRenameNodeButton, boolean checkable, boolean editable, boolean draggable,
            boolean sorted) {
        this(parent, name, description, title, table, hidden, hideCreateNodeButton, useDefaultCreateNodeButton,
                hideDeleteNodeButton, useDefaultDeleteNodeButton, hideRenameNodeButton, useDefaultRenameNodeButton,
                checkable, editable, draggable, sorted, false);
    }

    public Tree(Component parent, String name, String description, String title, BaseTableInterface<?> table,
            boolean hidden, boolean hideCreateNodeButton, boolean useDefaultCreateNodeButton,
            boolean hideDeleteNodeButton, boolean useDefaultDeleteNodeButton, boolean hideRenameNodeButton,
            boolean useDefaultRenameNodeButton, boolean checkable, boolean editable, boolean draggable, boolean sorted,
            boolean coloredIcons) {
        this(parent, name, description, title, table, hidden, hideCreateNodeButton, useDefaultCreateNodeButton,
                hideDeleteNodeButton, useDefaultDeleteNodeButton, hideRenameNodeButton, useDefaultRenameNodeButton,
                checkable, editable, draggable, sorted, coloredIcons, false);
    }

    public Tree(Component parent, String name, String description, String title, BaseTableInterface<?> table,
            boolean hidden, boolean hideCreateNodeButton, boolean useDefaultCreateNodeButton,
            boolean hideDeleteNodeButton, boolean useDefaultDeleteNodeButton, boolean hideRenameNodeButton,
            boolean useDefaultRenameNodeButton, boolean checkable, boolean editable, boolean draggable, boolean sorted,
            boolean coloredIcons, boolean activeContextMenu) {
        this(parent, name, description, title, table, hidden, hideCreateNodeButton, useDefaultCreateNodeButton,
                hideDeleteNodeButton, useDefaultDeleteNodeButton, hideRenameNodeButton, useDefaultRenameNodeButton,
                checkable, editable, draggable, sorted, coloredIcons, activeContextMenu, DATA_TYPES.HTML.name());
    }

    public Tree(Component parent, String name, String description, String title, BaseTableInterface<?> table,
            boolean hidden, boolean hideCreateNodeButton, boolean useDefaultCreateNodeButton,
            boolean hideDeleteNodeButton, boolean useDefaultDeleteNodeButton, boolean hideRenameNodeButton,
            boolean useDefaultRenameNodeButton, boolean checkable, boolean editable, boolean draggable, boolean sorted,
            boolean coloredIcons, boolean activeContextMenu, String dataType) {
        super(parent, name, description);
        this.hideCreateNodeButton = hideCreateNodeButton;
        this.hideDeleteNodeButton = hideDeleteNodeButton;
        this.hideRenameNodeButton = hideRenameNodeButton;
        this.useDefaultCreateNodeButton = useDefaultCreateNodeButton;
        this.useDefaultDeleteNodeButton = useDefaultDeleteNodeButton;
        this.useDefaultRenameNodeButton = useDefaultRenameNodeButton;
        this.checkable = checkable;
        this.editable = editable;
        this.draggable = draggable;
        this.sorted = sorted;
        this.title = title;
        this.hidden = hidden;
        if (table != null) {
            this.table = table;
        } else {
            table = new BaseTable();
        }
        this.coloredIcons = coloredIcons;
        this.activeContextMenu = activeContextMenu;
        this.dataType = dataType;
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
     *            dto row
     * 
     * @throws EMFError
     *             eccezione generica
     */
    public void copyToBean(BaseRowInterface row) throws EMFError {
        for (TreeField field : this) {
            TreeField<?> treeField = (TreeField<?>) field;
            if (treeField.getValue() != null) {
                row.setObject(treeField.getAlias(), treeField.getValue());
            }
        }
    }

    public TreeField<?> getColumn(String columnName) {
        for (TreeField<?> field : this) {
            if (field.getName().equalsIgnoreCase(columnName)) {
                return field;
            }
        }
        return null;

    }

    /**
     * @return the hideCreateNodeButton
     */
    public boolean isHideCreateNodeButton() {
        return hideCreateNodeButton;
    }

    /**
     * @param hideCreateNodeButton
     *            the hideCreateNodeButton to set
     */
    public void setHideCreateNodeButton(boolean hideCreateNodeButton) {
        this.hideCreateNodeButton = hideCreateNodeButton;
    }

    /**
     * @return the hideDeleteNodeButton
     */
    public boolean isHideDeleteNodeButton() {
        return hideDeleteNodeButton;
    }

    /**
     * @param hideDeleteNodeButton
     *            the hideDeleteNodeButton to set
     */
    public void setHideDeleteNodeButton(boolean hideDeleteNodeButton) {
        this.hideDeleteNodeButton = hideDeleteNodeButton;
    }

    /**
     * @return the hideRenameNodeButton
     */
    public boolean isHideRenameNodeButton() {
        return hideRenameNodeButton;
    }

    /**
     * @param hideRenameNodeButton
     *            the hideRenameNodeButton to set
     */
    public void setHideRenameNodeButton(boolean hideRenameNodeButton) {
        this.hideRenameNodeButton = hideRenameNodeButton;
    }

    /**
     * @return the checkable
     */
    public boolean isCheckable() {
        return checkable;
    }

    /**
     * @param checkable
     *            the checkable to set
     */
    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @param editable
     *            the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return the draggable
     */
    public boolean isDraggable() {
        return draggable;
    }

    /**
     * @param draggable
     *            the draggable to set
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     * @return the sorted
     */
    public boolean isSorted() {
        return sorted;
    }

    /**
     * @param sorted
     *            the sorted to set
     */
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden
     *            the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * @return the table
     */
    public BaseTableInterface<?> getTable() {
        return table;
    }

    /**
     * @param table
     *            the table to set
     */
    public void setTable(BaseTableInterface<?> table) {
        this.table = table;
    }

    /**
     * @return the useDefaultCreateNodeButton
     */
    public boolean isUseDefaultCreateNodeButton() {
        return useDefaultCreateNodeButton;
    }

    /**
     * @param useDefaultCreateNodeButton
     *            the useDefaultCreateNodeButton to set
     */
    public void setUseDefaultCreateNodeButton(boolean useDefaultCreateNodeButton) {
        this.useDefaultCreateNodeButton = useDefaultCreateNodeButton;
    }

    /**
     * @return the useDefaultDeleteNodeButton
     */
    public boolean isUseDefaultDeleteNodeButton() {
        return useDefaultDeleteNodeButton;
    }

    /**
     * @param useDefaultDeleteNodeButton
     *            the useDefaultDeleteNodeButton to set
     */
    public void setUseDefaultDeleteNodeButton(boolean useDefaultDeleteNodeButton) {
        this.useDefaultDeleteNodeButton = useDefaultDeleteNodeButton;
    }

    /**
     * @return the useDefaultRenameNodeButton
     */
    public boolean isUseDefaultRenameNodeButton() {
        return useDefaultRenameNodeButton;
    }

    /**
     * @param useDefaultRenameNodeButton
     *            the useDefaultRenameNodeButton to set
     */
    public void setUseDefaultRenameNodeButton(boolean useDefaultRenameNodeButton) {
        this.useDefaultRenameNodeButton = useDefaultRenameNodeButton;
    }

    /**
     * @return the coloredIcons
     */
    public boolean isColoredIcons() {
        return coloredIcons;
    }

    /**
     * @param coloredIcons
     *            the coloredIcons to set
     */
    public void setColoredIcons(boolean coloredIcons) {
        this.coloredIcons = coloredIcons;
    }

    /**
     * @return the activeContextMenu
     */
    public boolean isActiveContextMenu() {
        return activeContextMenu;
    }

    /**
     * @param activeContextMenu
     *            the activeContextMenu to set
     */
    public void setActiveContextMenu(boolean activeContextMenu) {
        this.activeContextMenu = activeContextMenu;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

}
