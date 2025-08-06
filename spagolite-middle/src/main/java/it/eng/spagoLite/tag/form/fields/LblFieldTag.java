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

package it.eng.spagoLite.tag.form.fields;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.impl.Button;
import it.eng.spagoLite.form.fields.impl.ComboBox;
import it.eng.spagoLite.form.fields.impl.Link;
import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class LblFieldTag extends FieldTag {
    private static final long serialVersionUID = 1L;

    private String width;
    private String position;

    public String getWidth() {
	if (width == null || "".equals(width)) {
	    switch (getColSpan()) {
	    case 1:
		return ContainerTag.WIDTH_1_CONTROLLER;
	    case 2:
		return ContainerTag.WIDTH_2_CONTROLLER;
	    case 3:
		return ContainerTag.WIDTH_3_CONTROLLER;
	    case 4:
		return ContainerTag.WIDTH_4_CONTROLLER;
	    default:
		return ContainerTag.WIDTH_50;
	    }

	}
	// return ContainerTag.WIDTH_50;
	// return ContainerTag.WIDTH_100;
	else {
	    return width;
	}
    }

    public void setWidth(String width) {
	this.width = width;
    }

    public String getPosition() {
	if (position != null && position.equalsIgnoreCase(ContainerTag.RIGHT))
	    return ContainerTag.RIGHT;
	else
	    return ContainerTag.LEFT;
    }

    public void setPosition(String position) {
	this.position = position;
    }

    // non essendoci ereditarietà multipla devo ridefinirli
    private String labelWidth;
    private String labelPosition;

    public String getLabelWidth() {
	if (labelWidth == null || "".equals(labelWidth)) {
	    if (ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition())) {
		// return ContainerTag.WIDTH_40;
		return ContainerTag.WIDTH_LABEL;
	    } else {
		return ContainerTag.WIDTH_60;
	    }
	} else {
	    return labelWidth;
	}
    }

    public void setLabelWidth(String labelWidth) {
	this.labelWidth = labelWidth;
    }

    public String getLabelPosition() {
	if (labelPosition != null && labelPosition.equalsIgnoreCase(ContainerTag.RIGHT))
	    return ContainerTag.RIGHT;
	else
	    return ContainerTag.LEFT;
    }

    public void setLabelPosition(String labelPosition) {
	this.labelPosition = labelPosition;
    }

    protected void writeStartContainer() throws JspException {
	writeln("");
	writeln(ContainerTag.Factory.htmlStartContainer(getWidth(), getPosition()));
    }

    /**
     * Stampa il container
     *
     * @throws JspException eccezione generica
     */
    protected void writeEndContainer() throws JspException {
	writeln(ContainerTag.Factory.htmlEndContainer());
    }

    public int doStartTag() throws JspException {
	return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
	try {
	    if (getColSpan() == 0)
		writeStartContainer();
	    writeln(LabelTag.Factory.htmlLabel(getComponent(), getLabelWidth(),
		    getLabelPosition()));
	    if (getColSpan() > 0)
		writeStartContainer();
	    if (getComponent() instanceof Button) {
		Button<?> button = (Button<?>) getComponent();
		String action = "button/" + getForm().getClass().getSimpleName() + "#"
			+ button.getParent().getName() + "/" + button.getName();
		if (button.isSecure() && isUserAuthorized(action)) {
		    debugAuthorization(action);
		    writeln(FieldTag.Factory.htmlField(button, getControlWidth(),
			    getControlPosition(), getActionName(), -1, isWithSearchcomp(),
			    getTooltip()));
		} else if (!button.isSecure()) {
		    writeln(FieldTag.Factory.htmlField(button, getControlWidth(),
			    getControlPosition(), getActionName(), -1, isWithSearchcomp(),
			    getTooltip()));
		}
	    } else if (getComponent() instanceof Link) {
		Link<?> link = (Link<?>) getComponent();
		String linkAction = "";
		if (link.isTargetAList()) {
		    throw new EMFError(EMFError.WARNING,
			    "Impossibile creare un link con attributo targetAList attivo esternamente a una lista");
		} else {
		    // il link è usato per eseguire un metodo ..
		    linkAction = "detail/" + getForm().getClass().getSimpleName() + "#"
			    + link.getParent().getName() + "/" + link.getTarget();
		}

		String name = JavaScript.stringToHTMLString(link.getName());
		String tooltip = JavaScript.stringToHTMLString(link.getTooltip());
		String className = ContainerTag.LEFT.equalsIgnoreCase(getControlPosition())
			? "slText " + getControlWidth()
			: "slTextRight " + getControlWidth();
		String value = (StringUtils.isNotBlank(link.getValue())) ? link.getHtmlValue() : "";

		if (link.isHidden()) {
		    className += " displayNone ";
		}

		if (isUserAuthorized(linkAction)) {
		    debugAuthorization(linkAction);
		    String linkString = "<a title=\"" + tooltip + "\" class=\"" + name
			    + "\" href=\"" + getOperationUrl(link.getTarget(), "riga=-1") + "\">"
			    + value + "</a>&nbsp;";
		    writeln(linkString);
		} else {
		    String hidden = " <input type=\"hidden\" name=\"" + name + "\" id=\"" + name
			    + "_hidden\" value=\"" + value + "\" />\n";
		    writeln(hidden + " <div  id=\"" + name + "\" class=\"" + className + "\" >"
			    + value + "</div>");
		}
	    } else {
		writeln(FieldTag.Factory.htmlField(getComponent(), getControlWidth(),
			getControlPosition(), getActionName(), -1, isWithSearchcomp(),
			getTooltip()));
		if ((getComponent() instanceof ComboBox)
			&& (isWithSearchcomp()/* from tag */ || ((ComboBox) getComponent())
				.isWithSearchComp())/* from component */) {
		    StringBuilder select2 = new StringBuilder();
		    String name = getComponent().getName();
		    Field field = (ComboBox<?>) getComponent();
		    // editable
		    if (!field.isReadonly() && field.isEditMode()) {
			select2.append(" <script type=\"text/javascript\" >" + "$('#" + name
				+ "').select2({theme: \"classic\", placeholder: '<i class=\"fa fa-search\"></i> Ricerca e seleziona elemento dalla lista...', allowClear: true, "
				+ " dropdownCssClass: \"select2-customdrop\", "
				+ "		   dropdownAutoWidth: true, width: 'auto', \"language\": \"it\", escapeMarkup: function(m){ return m; } })");
			if (getComponent().isTrigger()) {
			    select2.append(".on('change.select2',function() { "
				    + " newTriggerForSelect2('trigger"
				    + getComponent().getParent().getName() + name
				    + "OnTriggerAjax' );" + "})");
			}
			// prevent opening after clear selection {
			select2.append(
				".on('select2:unselecting', function () {  $(this).data('unselecting', true); })");
			select2.append(
				".on('select2:opening', function (e) {  if ($(this).data('unselecting')) {  $(this).removeData('unselecting'); e.preventDefault(); } });");
			select2.append("</script>");
			// } prevent opening after clear selection
			writeln(select2.toString());
			// html5 placeholder
			writeln(" <script type=\"text/javascript\" > " + "$('#" + name + "')"
				+ ".on(\"select2:open\", function(e) {$('input.select2-search__field').prop('placeholder', 'Effettua ricerca'); });"
				+ " </script>");
		    }
		}
	    }
	    writeEndContainer();
	} catch (EMFError e) {
	    throw new JspException(e);
	}

	return EVAL_PAGE;
    }

}
