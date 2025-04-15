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

package it.eng.spagoLite.tag.form.wizard;

import it.eng.spagoLite.form.wizard.Step;
import it.eng.spagoLite.form.wizard.Wizard;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

public class StepTag extends BaseFormTag<Step> {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
	Step step = getComponent();

	if (!step.isHidden()) {

	    writeln("<div class=\"step\">");
	    writeln("  <!-- Step " + step.getDescription() + "-->");

	    if (AbstractWizardTag.showContent((Wizard) getComponent().getParent(), step)) {
		return EVAL_BODY_INCLUDE;
	    }
	}

	return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
	if (!getComponent().isHidden()) {
	    writeln("</div>");
	}

	return EVAL_PAGE;
    }
}
