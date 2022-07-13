package it.eng.spagoLite.tag.prototype;

import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;

public class LblCheckTag extends AbstractLblTag {

    private static final long serialVersionUID = 1L;

    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    protected void writeControl() throws JspException {
        String checked = isChecked() ? "checked=\"checked\"" : "";
        String src = this.getContextPath() + (isChecked() ? "/img/checkbox-on.png" : "/img/checkbox-off.png");
        String className = ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition()) ? "slText " + getControlwidth()
                : "slTextRight " + getControlwidth();

        if (isEditable()) {
            writeln(" <div id=\"" + getId() + "\" class=\"" + className + "\" ><input id=\"" + getId() + "\" name=\""
                    + getId() + "\" type=\"checkbox\" " + checked + " /></div>");
        } else {
            writeln(" <div id=\"" + getId() + "\" class=\"" + className + "\" ><img src=\"" + src + "\" /></div>");
        }
    }

}
