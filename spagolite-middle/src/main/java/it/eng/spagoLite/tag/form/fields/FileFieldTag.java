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

package it.eng.spagoLite.tag.form.fields;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.fields.impl.Button;
import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

public class FileFieldTag extends FieldTag {
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
     * @throws JspException
     *             eccezione generica
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
            writeln(LabelTag.Factory.htmlLabel(getComponent(), getLabelWidth(), getLabelPosition()));
            if (getColSpan() > 0)
                writeStartContainer();
            if (getComponent() instanceof Button) {
                Button button = (Button) getComponent();
                String action = "button/" + getForm().getClass().getSimpleName() + "#" + button.getParent().getName()
                        + "/" + button.getName();
                if (button.isSecure() && isUserAuthorized(action)) {
                    debugAuthorization(action);
                    writeln(FieldTag.Factory.htmlField(button, getControlWidth(), getControlPosition(), getActionName(),
                            -1, isWithSearchcomp(), getTooltip()));
                } else if (!button.isSecure()) {
                    writeln(FieldTag.Factory.htmlField(button, getControlWidth(), getControlPosition(), getActionName(),
                            -1, isWithSearchcomp(), getTooltip()));
                }
            } else {
                writeln(FieldTag.Factory.htmlField(getComponent(), getControlWidth(), getControlPosition(),
                        getActionName(), -1, isWithSearchcomp(), getTooltip()));

            }
            writeEndContainer();
        } catch (EMFError e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

}
