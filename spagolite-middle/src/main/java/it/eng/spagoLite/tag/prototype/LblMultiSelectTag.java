package it.eng.spagoLite.tag.prototype;

import it.eng.spagoLite.tag.form.formLayout.ContainerTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;

/**
 * Stampa una Multi select statica
 */
public class LblMultiSelectTag extends AbstractLblSelectTag implements IterationTag {
    private static final long serialVersionUID = 1L;

    protected void writeControl() throws JspException {
        String className = ContainerTag.LEFT.equalsIgnoreCase(getLabelPosition()) ? "slText " + getControlwidth()
                : "slTextRight " + getControlwidth();

        if (isEditable()) {
            writeln(" <select multiple=\"multiple\" id=\"" + getId() + "\" name=\"" + getId() + "\" class=\""
                    + className + "\" style=\"height: 10em;\">" + getOption() + "</select>");
        } else {
            writeln(" <div class=\"" + className + "\" ></div>");
        }
    }

}
