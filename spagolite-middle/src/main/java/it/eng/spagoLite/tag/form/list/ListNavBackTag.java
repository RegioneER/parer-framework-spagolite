package it.eng.spagoLite.tag.form.list;

import it.eng.spagoLite.db.base.BaseTableInterface;

import javax.servlet.jsp.JspException;

/**
 * @deprecated Al momento non disegna nulla sulla pagina.
 * 
 *
 */
public class ListNavBackTag extends AbstractListNavBarTag {

    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();

        if (table != null) {
            if (table.size() == 0) {
                return SKIP_BODY;
            }
            // writeln("<div>");
            // writeBack();
            // writeln("</div>");
        }

        return SKIP_BODY;
    }
}