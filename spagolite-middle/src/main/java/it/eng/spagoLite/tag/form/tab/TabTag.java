package it.eng.spagoLite.tag.form.tab;

import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoIFace.Values;
import it.eng.spagoLite.form.base.BaseElements;
import it.eng.spagoLite.form.base.BaseElements.Status;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.form.list.NestedList;
import it.eng.spagoLite.form.tab.Tab;
import it.eng.spagoLite.form.tab.TabElement;
import it.eng.spagoLite.tag.NewLineTag;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

public class TabTag extends BaseFormTag<Tab> {

    private static final long serialVersionUID = 1L;

    private String tabElement;

    private boolean isViewMode() {
        BaseElements<?> masterList = getComponent().getMasterList();
        boolean result = true;
        if (masterList instanceof List<?>) {
            result = ((List<?>) masterList).getStatus().equals(Status.view);
        } else if (masterList instanceof NestedList<?>) {
            result = ((NestedList<?>) masterList).getStatus().equals(Status.view);
        }
        return result;
    }

    @Override
    public int doStartTag() throws JspException {
        Tab tab = getComponent();
        if (tabElement != null && !tabElement.equals(tab.getCurrentTab().getName()))
            return SKIP_BODY;
        writeln("");
        writeln("<!-- Tabs Tag -->");
        writeln(NewLineTag.Factory.htmlNewLine());
        writeln("<div class=\"tabs\">");

        for (TabElement tabElement : tab) {
            if (tabElement.isHidden()) {
                continue;
            }

            String name = JavaScript.stringToHTMLString(tabElement.getName());
            String description = JavaScript.stringToHTMLString(tabElement.getDescription());

            String iconHtml = "";
            if (tabElement.getIconUrlList() != null) {

                for (String iconUrl : tabElement.getIconUrlList()) {
                    if (iconUrl != null && !"".equals(iconUrl) && !"null".equalsIgnoreCase(iconUrl))
                        iconHtml += "<img src=\"" + iconUrl + "\" alt=\"" + description + "\" title=\"" + description
                                + "\" />";

                }
            }

            String query = Values.OPERATION + Values.OPERATION_SEPARATOR + "tab" + name + "OnClick";

            if (isViewMode()) {
                if (tabElement == tab.getCurrentTab()) {
                    writeln(" <span class=\"current\"><span>" + iconHtml + description + "</span></span>");
                } else {
                    writeln(" <span class=\"nonCurrent\">" + iconHtml + "<input type=\"submit\" value=\"" + description
                            + "\" id=\"" + name + "\" title=\"" + description + "\" name=\"" + query + "\"/></span>");
                }
            } else {
                if (tabElement == tab.getCurrentTab()) {
                    writeln(" <span class=\"current\" id=\"" + name + "\"><span>" + iconHtml + description
                            + "</span></span>");
                } else {
                    writeln(" <span class=\"nonCurrent\" id=\"" + name + "\"><span>" + iconHtml + description
                            + "</span></span>");
                }
            }
        }
        writeln("</div>");
        writeln("<div class=\"tabs-content\">");
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        Tab tab = getComponent();
        if (tabElement != null && !tabElement.equals(tab.getCurrentTab().getName()))
            return EVAL_PAGE;
        writeln("</div>");
        return EVAL_PAGE;
    }

    public String getTabElement() {
        return tabElement;
    }

    public void setTabElement(String tabElement) {
        this.tabElement = tabElement;
    }

}
