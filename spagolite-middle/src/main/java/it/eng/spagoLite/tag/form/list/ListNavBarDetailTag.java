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

package it.eng.spagoLite.tag.form.list;

import it.eng.spagoLite.actions.form.ListAction;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.form.list.List;

import javax.servlet.jsp.JspException;

public class ListNavBarDetailTag extends AbstractListNavBarTag {

    private static final long serialVersionUID = 1L;

    private boolean hideOperationButton;

    private String insertAction = "toolbar/insert";
    private String editAction = "toolbar/edit";
    private String deleteAction = "toolbar/delete";

    @Override
    public int doStartTag() throws JspException {
        if (getName() != null) {

            BaseTableInterface<?> table = getComponent().getTable();

            if (table != null) {
                if (table.size() == 0) {
                    return SKIP_BODY;
                }
                writeln("<div class=\"listToolBar\">");
                calculateAuthorization();

                // Pulsanti di modifica/salvataggio
                if (!getComponent().isReadOnly() && !isHideOperationButton()) {
                    if ((isEditAction() && !getComponent().isHideUpdateButton())
                            || ((isInsertAction()) && (!((List) getComponent()).isHideInsertButton()))) {
                        debugAuthorization(insertAction);
                        debugAuthorization(editAction);
                        writeDetailSave();
                    }
                    if (isEditAction() && !getComponent().isHideUpdateButton()) {
                        debugAuthorization(editAction);
                        writeDetailUpdate();
                        writeDetailCancel();
                    }
                    if (isDeleteAction() && !getComponent().isHideDeleteButton()) {
                        debugAuthorization(deleteAction);
                        writeDetailDelete();
                    }
                }

                // Pulsanti di navigazione
                writeElenco();
                writePrev();
                writeRecordCounter();
                writeNext();

            }
        } else {
            writeln("<div class=\"listToolBar\">");
            writeln(getLink("navBarBackLink", ListAction.NE_ELENCO, "", "Indietro", "Indietro", -1, false, true));
        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        writeln("</div>");
        return super.doEndTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        // TODO Auto-generated method stub
        return super.doAfterBody();
    }

    public boolean isHideOperationButton() {
        return hideOperationButton;
    }

    public void setHideOperationButton(boolean hideOperationButton) {
        this.hideOperationButton = hideOperationButton;
    }

    private void calculateAuthorization() {

        setEditAction(isUserAuthorized(editAction));
        setDeleteAction(isUserAuthorized(deleteAction));
        setInsertAction(isUserAuthorized(insertAction));
    }

}
