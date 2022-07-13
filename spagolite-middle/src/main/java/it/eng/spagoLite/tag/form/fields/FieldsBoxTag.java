package it.eng.spagoLite.tag.form.fields;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.Fields;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.tag.NewLineTag;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

public class FieldsBoxTag extends BaseFormTag<Fields<Field>> {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        try {

            writeln("");
            writeln("<!-- Box -->");

            Fields<Field> fields = getComponent();
            int i = 0;
            for (Field field : fields) {
                if (field instanceof SingleValueField) {
                    SingleValueField<?> singleValueField = (SingleValueField<?>) field;
                    String description = singleValueField.getHtmlDescription();
                    String value = singleValueField.getHtmlDecodedValue();
                    if (!singleValueField.isHidden()) {
                        if (i++ == 0) {
                            writeln("<div class=\"sintTitolo\"><b>" + description + ": </b>" + value + "</div>");
                        } else {
                            writeln("<div class=\"sintRiga\"><b>" + description + ": </b>" + value + "</div>");
                        }
                        writeln(NewLineTag.Factory.htmlNewLine());
                    }

                }
            }
        } catch (EMFError e) {
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
