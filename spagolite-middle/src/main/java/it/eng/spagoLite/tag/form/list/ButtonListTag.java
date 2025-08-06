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

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.buttonList.ButtonList;
import it.eng.spagoLite.form.fields.impl.Button;
import it.eng.spagoLite.security.BaseUser;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.security.profile.Pagina;
import it.eng.spagoLite.tag.form.BaseFormTag;
import it.eng.spagoLite.tag.form.fields.FieldTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class ButtonListTag extends BaseFormTag<ButtonList> {

    private static final long serialVersionUID = 1L;

    private boolean disableAll;

    @Override
    public int doStartTag() throws JspException {
	try {
	    ButtonList buttonList = (ButtonList) getComponent();
	    if (buttonList != null) {
		for (Button<?> button : buttonList) {
		    String action = "button/" + getForm().getClass().getSimpleName() + "#"
			    + buttonList.getName() + "/" + button.getName();
		    if (button.isSecure() && isUserAuthorized(action)) {
			debugAuthorization(action);
			writeln(FieldTag.Factory.htmlField(button, null, null, getActionName(), -1,
				false, StringUtils.EMPTY));
		    } else if (!button.isSecure()) {
			writeln(FieldTag.Factory.htmlField(button, null, null, getActionName(), -1,
				false, StringUtils.EMPTY));
		    }
		}
	    }
	} catch (EMFError e) {
	    e.printStackTrace();
	}

	return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
	return EVAL_PAGE;
    }

    public boolean isDisableAll() {
	return disableAll;
    }

    public void setDisableAll(boolean disableAll) {
	this.disableAll = disableAll;
    }

}
