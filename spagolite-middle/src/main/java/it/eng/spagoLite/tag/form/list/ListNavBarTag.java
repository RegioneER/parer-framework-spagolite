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

package it.eng.spagoLite.tag.form.list;

import it.eng.spagoLite.db.base.BaseTableInterface;

import javax.servlet.jsp.JspException;

public class ListNavBarTag extends AbstractListNavBarTag {

    private static final long serialVersionUID = 1L;

    private boolean navigableRecord = false;
    private boolean exportExcel = true;
    private boolean pageSizeRelated = false;

    @Override
    public int doStartTag() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        calculateAuthorization();
        if (table != null) {
            if (!isPageSizeRelated() && table.size() == 0) {
                if (isInsertAction() && !getComponent().isHideInsertButton()) {
                    debugAuthorization(getInsertAction());
                    writeln("<div class=\"listToolBar\">&nbsp;");
                    writeInsert();
                    if (getComponent().isFilterValidRecords() != null) {
                        if (getComponent().isFilterValidRecords()) {
                            writeShowInactiveRecords();
                        } else {
                            writeHideInactiveRecords();
                        }
                    }
                    writeln("</div>");
                } else if (getComponent().isFilterValidRecords() != null) {
                    writeln("<div class=\"listToolBar\">&nbsp;");
                    if (getComponent().isFilterValidRecords()) {
                        writeShowInactiveRecords();
                    } else {
                        writeHideInactiveRecords();
                    }
                    writeln("</div>");
                }
                return SKIP_BODY;
            }
            if ((isPageSizeRelated() && table.getPageSize() <= 10)
                    || (isPageSizeRelated() && table.size() < table.getPageSize())) {
                return SKIP_BODY;
            }
            writeln("<div class=\"listToolBar\">");
            // allineati a destra, explorer 7 non gestisce bene il
            // float:right...
            if (!isPageSizeRelated() && isExportExcel()) {
                writeExcel();
                // writeSeparator();
            }
            // writeDelete();
            // writeUpdate();
            if (!isPageSizeRelated() && isInsertAction() && !getComponent().isHideInsertButton()) {
                debugAuthorization(getInsertAction());
                writeInsert();
            }
            // allineati a sinistra
            writeFirst();
            writePrevPage();
            if (isNavigableRecord()) {
                writePrev();
                writePageCounter();
                writeNext();
            } else {
                writePageCounter();
            }
            writeNextPage();
            writeLast();

            // MEV 33070
            if (getComponent().isGoToPageNavigation() != null) {
                if (getComponent().isGoToPageNavigation()) {
                    writeGoToPage();
                }
            }
            // end MEV 33070

            if (isNavigableRecord()) {
                writeRecordCounter();
            } else {
                writeRecordNumber();
            }
            writeIsListSortable();
            writeRecordPerPage();

            if (getComponent().isFilterValidRecords() != null) {
                if (getComponent().isFilterValidRecords()) {
                    writeShowInactiveRecords();
                } else {
                    writeHideInactiveRecords();
                }
            }
            writeln("</div>");
        }

        return SKIP_BODY;
    }

    public boolean isNavigableRecord() {
        return navigableRecord;
    }

    public void setNavigableRecord(boolean navigableRecord) {
        this.navigableRecord = navigableRecord;
    }

    public boolean isExportExcel() {
        return exportExcel && isListInMemory();
    }

    public void setExportExcel(boolean exportExcel) {
        this.exportExcel = exportExcel;
    }

    private void calculateAuthorization() {
        setInsertAction(isUserAuthorized(getInsertAction()));
    }

    private String getInsertAction() {
        return "detail/" + getForm().getClass().getSimpleName() + "#" + getName() + "/insert";
    }

    public boolean isPageSizeRelated() {
        return pageSizeRelated;
    }

    public void setPageSizeRelated(boolean pageSizeRelated) {
        this.pageSizeRelated = pageSizeRelated;
    }

}
