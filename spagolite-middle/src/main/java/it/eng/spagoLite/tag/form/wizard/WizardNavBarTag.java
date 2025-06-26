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

package it.eng.spagoLite.tag.form.wizard;

import javax.servlet.jsp.JspException;

public class WizardNavBarTag extends AbstractWizardTag {

    private static final long serialVersionUID = 1L;

    private String insertAction = "toolbar/insert";
    private String editAction = "toolbar/edit";

    @Override
    public int doStartTag() throws JspException {
        if (getComponent().isEndPage() && getComponent().getEndPage().isHideBar()) {
            return SKIP_BODY;
        }

        writeln("<div class=\"wizardBar\">");
        calculateAuthorization();

        writeCancel();
        if (isEditAction() || isInsertAction()) {
            debugAuthorization(insertAction);
            debugAuthorization(editAction);
            writeSave();
        }
        writePrev();
        writeNext();

        writeln("&nbsp;</div>");

        return SKIP_BODY;
    }

    private void calculateAuthorization() {
        setEditAction(isUserAuthorized(editAction));
        setInsertAction(isUserAuthorized(insertAction));
    }
}
