/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.spagoLite.tag.form.fields;

import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.Fields;
import it.eng.spagoLite.security.CsrfHelper;
import it.eng.spagoLite.tag.form.BaseFormTag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import static javax.servlet.jsp.tagext.Tag.EVAL_PAGE;

/**
 *
 * @author sinatti_s
 */
public class CsrfInputTag extends BaseFormTag<Fields<Field>> {

    @Override
    public int doEndTag() throws JspException {
        writeln(CsrfHelper.getCsrfInputToken((HttpServletRequest) this.pageContext.getRequest()));
        return EVAL_PAGE;
    }

}
