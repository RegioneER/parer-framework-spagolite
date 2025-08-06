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

package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.Button;
import it.eng.spagoLite.xmlbean.form.ButtonList;
import it.eng.spagoLite.xmlbean.form.CheckBox;
import it.eng.spagoLite.xmlbean.form.ComboBox;
import it.eng.spagoLite.xmlbean.form.Field;
import it.eng.spagoLite.xmlbean.form.Field.Type;
import it.eng.spagoLite.xmlbean.form.Input;
import it.eng.spagoLite.xmlbean.form.Link;
import it.eng.spagoLite.xmlbean.form.MultiSelect;
import it.eng.spagoLite.xmlbean.form.MultiSemaphore;
import it.eng.spagoLite.xmlbean.form.Radio;
import it.eng.spagoLite.xmlbean.form.Semaphore;
import it.eng.spagoLite.xmlbean.form.TextArea;
import it.eng.util.formWriter.base.ElementWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.StringUtils;

public class FieldWriter extends ElementWriter<Field> {

    public FieldWriter(Field element) {
	super(element);
    }

    private String getDataType() {
	String dataType = "<String>";
	if (getElement().getType().equals(Type.STRING)) {
	    dataType = "<String>";
	} else if (getElement().getType().equals(Type.DATE)) {
	    dataType = "<Timestamp>";
	} else if (getElement().getType().equals(Type.DATETIME)) {
	    dataType = "<Timestamp>";
	} else if (getElement().getType().equals(Type.INTEGER)) {
	    dataType = "<BigDecimal>";
	} else if (getElement().getType().equals(Type.DECIMAL)) {
	    dataType = "<BigDecimal>";
	} else if (getElement().getType().equals(Type.CURRENCY)) {
	    dataType = "<BigDecimal>";
	}

	return dataType;
    }

    public void writeAdd(Writer writer) throws IOException {
	String description = getElement().getDescription();
	String alias = getElement().getAlias() == null ? "" : getElement().getAlias();
	String format = getElement().getFormat() == null ? "null"
		: "\"" + getElement().getFormat() + "\"";
	String type = getElement().getType() != null ? "Type." + getElement().getType() : "null";

	if (getElement() instanceof Input) {
	    Input input = (Input) getElement();
	    String forceCase = input.getForceCase() != null ? "ForceCase." + input.getForceCase()
		    : "null";
	    String forceTrim = input.getForceCase() != null ? "ForceTrim." + input.getForceTrim()
		    : "null";
	    String regExp = input.getRegExp() != null ? "\"" + input.getRegExp() + "\"" : "null";
	    String groupingDecimal = input.isSetGroupingDecimal() ? "" + input.getGroupingDecimal()
		    : "false";
	    String tooltip = input.getTooltip() != null ? "\"" + input.getTooltip() + "\"" : "null";

	    writer.write("      addComponent(new Input" + getDataType() + "(this, "
		    + getConstantName().toLowerCase() + ", \"" + description + "\", \"" + alias
		    + "\"," + type + ", " + format + ", " + groupingDecimal + ", "
		    + input.getRequired() + ", " + input.getHidden() + ", " + input.getReadonly()
		    + ", " + input.getTrigger() + ", " + input.getMaxLength() + ", " + forceCase
		    + ", " + forceTrim + ", " + regExp + ", " + tooltip + "));\n");
	} else if (getElement() instanceof ComboBox) {
	    ComboBox comboBox = (ComboBox) getElement();
	    boolean addBlank = comboBox.isSetAddBlank() ? comboBox.getAddBlank() : true;
	    boolean withsearchcomp = comboBox.isSetWithSearchComp() ? comboBox.getWithSearchComp()
		    : false;
	    writer.write("      addComponent(new ComboBox" + getDataType() + "(this, "
		    + getConstantName().toLowerCase() + ", \"" + description + "\", \"" + alias
		    + "\"," + type + ", " + format + ", " + comboBox.getRequired() + ", "
		    + comboBox.getHidden() + ", " + comboBox.getReadonly() + ", "
		    + comboBox.getTrigger() + ", " + comboBox.getMaxLength() + ", " + addBlank
		    + ", " + withsearchcomp + "));\n");
	} else if (getElement() instanceof CheckBox) {
	    CheckBox checkBox = (CheckBox) getElement();
	    String defaultValue = checkBox.isSetDefaultValue()
		    ? "\"" + checkBox.getDefaultValue().toString() + "\""
		    : null;
	    String tooltip = checkBox.getTooltip() != null ? "\"" + checkBox.getTooltip() + "\""
		    : "null";
	    if (StringUtils.isNotBlank(defaultValue)) {
		writer.write("      addComponent(new CheckBox" + getDataType() + "(this, "
			+ getConstantName().toLowerCase() + ", \"" + description + "\", \"" + alias
			+ "\"," + type + ", " + format + ", " + checkBox.getRequired() + ", "
			+ checkBox.getHidden() + ", " + checkBox.getReadonly() + ", "
			+ checkBox.getTrigger() + ", " + defaultValue + ", " + tooltip + "));\n");
	    } else {
		writer.write("      addComponent(new CheckBox" + getDataType() + "(this, "
			+ getConstantName().toLowerCase() + ", \"" + description + "\", \"" + alias
			+ "\"," + type + ", " + format + ", " + checkBox.getRequired() + ", "
			+ checkBox.getHidden() + ", " + checkBox.getReadonly() + ", "
			+ checkBox.getTrigger() + ", " + tooltip + "));\n");
	    }
	} else if (getElement() instanceof MultiSelect) {
	    MultiSelect multiSelect = (MultiSelect) getElement();
	    writer.write("      addComponent(new MultiSelect(this, "
		    + getConstantName().toLowerCase() + ", \"" + description + "\", \"" + alias
		    + "\"," + type + ", " + format + ", " + multiSelect.getRequired() + ", "
		    + multiSelect.getHidden() + ", " + multiSelect.getReadonly() + ", "
		    + multiSelect.getTrigger() + ", " + multiSelect.getMaxLength() + "));\n");
	} else if (getElement() instanceof TextArea) {
	    TextArea textArea = (TextArea) getElement();
	    writer.write("      addComponent(new TextArea" + getDataType() + "(this, "
		    + getConstantName().toLowerCase() + ", \"" + description + "\", \"" + alias
		    + "\"," + type + ", " + format + ", " + textArea.getRequired() + ", "
		    + textArea.getHidden() + ", " + textArea.getReadonly() + ", "
		    + textArea.getTrigger() + ", " + textArea.getRows() + ", " + textArea.getCols()
		    + ", " + textArea.getMaxLength() + ", " + textArea.getTooltip() + "));\n");
	} else if (getElement() instanceof Button) {
	    Button button = (Button) getElement();
	    writer.write("      addComponent(new Button(this, " + getConstantName().toLowerCase()
		    + ", \"" + description + "\", \"" + alias + "\"," + type + ", " + format + ", "
		    + button.getRequired() + ", " + button.getHidden() + ", " + button.getReadonly()
		    + ", " + button.getTrigger() + "," + button.getDisableHourGlass() + ","
		    + button.getSecure() + "));\n");
	} else if (getElement() instanceof Radio) {
	    Radio radio = (Radio) getElement();
	    writer.write("      addComponent(new Radio(this, " + getConstantName().toLowerCase()
		    + ", \"" + description + "\", \"" + alias + "\"," + type + ", " + format + ", "
		    + radio.getRequired() + ", " + radio.getHidden() + ", " + radio.getReadonly()
		    + ", " + radio.getTrigger() + ", \"" + radio.getGroupName() + "\"));\n");
	} else if (getElement() instanceof Semaphore) {
	    Semaphore semaphore = (Semaphore) getElement();
	    writer.write("      addComponent(new Semaphore(this, " + getConstantName().toLowerCase()
		    + ", \"" + description + "\", \"" + alias + "\"," + type + ", " + format + ", "
		    + semaphore.getRequired() + ", " + semaphore.getHidden() + ", "
		    + semaphore.getReadonly() + ", " + semaphore.getTrigger() + ","
		    + semaphore.getState() + "));\n");
	} else if (getElement() instanceof ButtonList) {
	    ButtonList buttonList = (ButtonList) getElement();
	    writer.write(
		    "      addComponent(new ButtonList(this, " + getConstantName().toLowerCase()
			    + ", \"" + description + ", \"" + buttonList.getDisableAll() + "));\n");
	} else if (getElement() instanceof MultiSemaphore) {
	    MultiSemaphore element = (MultiSemaphore) getElement();
	    writer.write("      addComponent(new MultiSemaphore(this, "
		    + getConstantName().toLowerCase() + ", \"" + description + "\", \"" + alias
		    + "\"," + type + ", " + format + ", " + element.getRequired() + ", "
		    + element.getHidden() + ", " + element.getReadonly() + ", "
		    + element.getTrigger() + ", " + element.getGreenChecked() + ", "
		    + element.getYellowChecked() + ", " + element.getRedChecked() + "));\n");
	} else if (getElement() instanceof Link) {
	    Link link = (Link) getElement();
	    // FIXME : re-factor richiesto dei parametri
	    String visibility = link.getVisibilityProperty() == null ? "(String) null"
		    : "\"" + link.getVisibilityProperty() + "\"";
	    String externalLink = ",(String) null";
	    if (StringUtils.isNotBlank(link.getExternalLinkParamApplic())) {
		String externalLinkParamId = link.getExternalLinkParamId() == null ? "(String) null"
			: "\"" + link.getExternalLinkParamId() + "\"";
		externalLink = ", \"" + link.getExternalLinkParamApplic() + "\", "
			+ externalLinkParamId;
	    }
	    String genericLinkId = StringUtils.isBlank(link.getGenericLinkId()) ? "(String) null"
		    : "\"" + link.getGenericLinkId() + "\"";
	    writer.write("      addComponent(new Link(this, " + getConstantName().toLowerCase()
		    + ", \"" + description + "\", \"" + alias + "\"," + type + ", " + format + ", "
		    + link.getRequired() + ", " + link.getHidden() + ", " + link.getReadonly()
		    + ", " + link.getTrigger() + ", \"" + link.getTarget() + "\", \""
		    + link.getTooltip() + "\", " + link.getIsTargetList() + ", " + visibility
		    + externalLink + "," + genericLinkId + "));\n");
	}
    }

    public void writeGet(Writer writer) throws IOException {
	String fieldType = "";

	if (getElement() instanceof Input) {
	    fieldType = "Input" + getDataType();
	} else if (getElement() instanceof ComboBox) {
	    fieldType = "ComboBox" + getDataType();
	} else if (getElement() instanceof CheckBox) {
	    fieldType = "CheckBox" + getDataType();
	} else if (getElement() instanceof MultiSelect) {
	    fieldType = "MultiSelect" + getDataType();
	} else if (getElement() instanceof TextArea) {
	    fieldType = "TextArea" + getDataType();
	} else if (getElement() instanceof Button) {
	    fieldType = "Button" + getDataType();
	} else if (getElement() instanceof Link) {
	    fieldType = "Link" + getDataType();
	} else if (getElement() instanceof Radio) {
	    fieldType = "Radio";
	} else if (getElement() instanceof Semaphore) {
	    fieldType = "Semaphore";
	} else if (getElement() instanceof MultiSemaphore) {
	    fieldType = "MultiSemaphore";
	}

	writer.write("    public " + fieldType + " get" + getClassName() + "() {\n");
	writer.write("      return (" + fieldType + ") getComponent("
		+ getConstantName().toLowerCase() + ");\n");
	writer.write("    }\n");
	writer.write("\n");

    }

}
