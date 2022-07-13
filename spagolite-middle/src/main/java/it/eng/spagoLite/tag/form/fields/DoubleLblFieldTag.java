package it.eng.spagoLite.tag.form.fields;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

public class DoubleLblFieldTag extends LblFieldTag {
    private static final long serialVersionUID = 1L;

    private String name2;
    private String controlWidth2;

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getControlWidth2() {
        if (controlWidth2 == null || controlWidth2.equals("")) {
            if (ContainerTag.LEFT.equalsIgnoreCase(getControlPosition()))
                return ContainerTag.WIDTH_60;
            else
                return ContainerTag.WIDTH_40;
        } else {
            return controlWidth2;
        }
    }

    public void setControlWidth2(String controlWidth2) {
        this.controlWidth2 = controlWidth2;
    }

    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        try {

            if (getColSpan() == 0)
                writeStartContainer();

            writeln(LabelTag.Factory.htmlLabel(getComponent(getName2()), getLabelWidth(), getLabelPosition()));
            if (getColSpan() > 0)
                writeStartContainer();
            writeln(FieldTag.Factory.htmlField(getComponent(), getControlWidth(), getControlPosition(), getActionName(),
                    -1, isWithSearchcomp(), getTooltip()));
            writeln(FieldTag.Factory.htmlField(getComponent(getName2()), getControlWidth2(), getControlPosition(),
                    getActionName(), -1, isWithSearchcomp(), getTooltip()));

            writeEndContainer();
        } catch (EMFError e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }
}
