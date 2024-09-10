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

package it.eng.spagoLite.tag.prototype;

import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.tag.BaseSpagoLiteTag;
import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

/**
 *
 * @author Enrico Grillini
 *
 */
public abstract class AbstractLblTag extends BaseSpagoLiteTag {

    private static final long serialVersionUID = 1L;

    private String label;
    private boolean editable;
    private String width;
    private String labelWidth;
    private String controlwidth;
    private String position;
    private String labelPosition;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getWidth() {
        if (width == null || "".equals(width))
            return ContainerTag.WIDTH_50;
        else
            return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLabelWidth() {
        if (labelWidth == null || "".equals(labelWidth)) {
            if (ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition()))
                return ContainerTag.WIDTH_40;
            else
                return ContainerTag.WIDTH_60;
        } else {
            return labelWidth;
        }
    }

    public void setLabelWidth(String labelWidth) {
        this.labelWidth = labelWidth;
    }

    public String getControlwidth() {
        if (controlwidth == null || controlwidth.equals("")) {
            if (ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition()))
                return ContainerTag.WIDTH_60;
            else
                return ContainerTag.WIDTH_40;
        } else {
            return controlwidth;
        }
    }

    public void setControlwidth(String controlwidth) {
        this.controlwidth = controlwidth;
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

    public String getLabelPosition() {
        if (labelPosition != null && labelPosition.equalsIgnoreCase(ContainerTag.RIGHT))
            return ContainerTag.RIGHT;
        else
            return ContainerTag.LEFT;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    /**
     * Scrive la label
     *
     * @throws JspException
     *             eccezione generica
     */
    protected void writeLabel() throws JspException {
        String label = ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition())
                ? JavaScript.stringToHTMLString(getLabel()) + ":&nbsp;" : JavaScript.stringToHTMLString(getLabel());
        String className = ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition()) ? "slLabel " + getLabelWidth()
                : "slLabelRight " + getLabelWidth();

        if (isEditable()) {
            writeln(" <label for=\"" + getId() + "\" class=\"" + className + "\">" + label + "</label>");
        } else {
            writeln(" <label for=\"" + getId() + "\" class=\"" + className + "\">" + label + "</label>");
        }
    }

    /**
     * Scrive il controllo
     *
     * @throws JspException
     *             eccezione generica
     */
    protected abstract void writeControl() throws JspException;

    /**
     * Stampa il container
     *
     * @throws JspException
     *             eccezione generica
     */
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
        writeStartContainer();

        if (ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition())) {
            writeLabel();
            writeControl();
        } else {
            writeControl();
            writeLabel();
        }
        writeEndContainer();

        return super.doEndTag();
    }

}
