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

package it.eng.spagoLite.tag.form.wizard;

import it.eng.spagoIFace.Values;
import it.eng.spagoLite.form.wizard.EndPage;
import it.eng.spagoLite.form.wizard.Step;
import it.eng.spagoLite.form.wizard.Wizard;
import it.eng.spagoLite.form.wizard.WizardElement;
import it.eng.spagoLite.tag.form.BaseFormTag;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang3.StringUtils;

public class AbstractWizardTag extends BaseFormTag<Wizard> {

    private static final long serialVersionUID = 1L;
    private boolean editAction;
    private boolean insertAction;

    protected String PREV_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/prevDisabled.gif\" title=\"Vai al record precedente\" alt=\"Vai alla pagina precedente\" />";
    protected String NEXT_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/nextDisabled.gif\" title=\"Vai record successivo\" alt=\"Vai record successivo\" />";
    protected String SAVE_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/saveDisabled.png\" title=\"Salva\" alt=\"Salva\"/>";

    protected Step getCurrentStep() {
        return getComponent().getCurrentStep();
    }

    protected String getCurrentElementName() {
        return getComponent().getCurrentElement().getName();
    }

    protected List<Step> getSteps() {
        return getComponent().getStepList();
    }

    protected EndPage getEndPage() {
        return getComponent().getEndPage();
    }

    protected boolean hasSummary() {
        List<Step> steps = getSteps();
        for (Step step : steps) {
            if (step.isSummary()) {
                return true;
            }
        }
        return false;
    }

    protected void writeSave() throws JspException {
        if (getComponent().isLastStep() || getComponent().isEndPage()) {
            writeln("<span class=\"floatRight\">"
                    + getSubmit(Wizard.WizardNavigation.Save.toString(), "crud save", "Salva", getCurrentElementName())
                    + "</span>");
        } else {
            writeln("<span class=\"floatRight\">&nbsp;" + SAVE_DISABLED + "&nbsp;Salva&nbsp;</span>");
        }
    }

    protected void writeCancel() throws JspException {
        writeln("<span class=\"floatRight\">" + getSubmit(Wizard.WizardNavigation.Cancel.toString(), "crud cancel",
                "Annulla", getCurrentElementName()) + "</span>");
    }

    protected void writePrev() throws JspException {
        if (getComponent().isFirstStep()) {
            // writeln("<span class=\"floatLeft\">&nbsp;" + PREV_DISABLED + "&nbsp;Indietro&nbsp;</span>");

        } else {
            writeln("<span class=\"floatLeft\">" + getSubmit(Wizard.WizardNavigation.Prev.toString(), "crud prev",
                    "Indietro", getCurrentElementName()) + "</span>");

        }
    }

    protected void writeNext() throws JspException {
        if (getComponent().isLastStep() || getComponent().isEndPage()) {
            // writeln("<span class=\"floatLeft\">&nbsp;" + NEXT_DISABLED + "&nbsp;Avanti&nbsp;</span>");
        } else {
            writeln("<span class=\"floatLeft\">"
                    + getSubmit(Wizard.WizardNavigation.Next.toString(), "crud next", "Avanti", getCurrentElementName())
                    + "</span>");
        }
    }

    /**
     * Costruisce un link in html.
     *
     * @deprecated
     *
     * @param navigationEvent
     *            evento di navigazione
     * @param linkClass
     *            rif. classe css
     * @param title
     *            titolo
     * @param step
     *            nome step
     * @param textInnerLink
     *            testo link
     *
     * @return link elaborato
     *
     * @throws JspException
     *             eccezione generica
     */
    @Deprecated
    protected String getLink(String navigationEvent, String linkClass, String title, String step, String textInnerLink)
            throws JspException {
        String submitName = "operation" + Values.OPERATION_SEPARATOR + "wizardNavigationOnClick"
                + Values.OPERATION_SEPARATOR + getName() + Values.OPERATION_SEPARATOR + navigationEvent;
        if (step != null) {
            submitName += Values.OPERATION_SEPARATOR + step;
        }
        String styleClass = StringUtils.isBlank(linkClass) ? "" : " class=\"" + linkClass + "\" ";
        String tagContent = (textInnerLink != null ? textInnerLink : "");
        tagContent = tagContent.equals("") ? "&nbsp;" : tagContent;
        if (navigationEvent != null) {
            // String href = getOperationUrl("wizardNavigationOnClick", "wizard=" + getName() + "&amp;navigationEvent="
            // + navigationEvent + "&amp;step=" + step);
            // if (step == null) {
            // href+= "&amp;"+ step;
            // }
            String href = "javascript:$('#spagoLiteAppForm').submit();";
            String hidden1 = "<input type=\"hidden\" name=\"wizard\" value=\"" + getName() + "\" />";
            String hidden2 = "<input type=\"hidden\" name=\"step\" value=\"" + step + "\" />";
            String hidden3 = "<input type=\"hidden\" name=\"navigationEvent\" value=\"" + navigationEvent + "\" />";
            String hidden4 = "<input type=\"hidden\" name=\"operation\" value=\"wizardNavigationOnClick\" />";
            return hidden1 + hidden2 + hidden3 + hidden4 + "<a " + styleClass + " name=\"" + submitName + "\""
                    + (title != null ? "title=\"" + title + "\"" : "") + " href=\"" + href + "\">" + tagContent
                    + "</a>&nbsp;";
        } else {
            return "<span " + styleClass + ">" + tagContent + "&nbsp;</span>";
        }
    }

    protected String getSubmit(String navigationEvent, String style, String title, String additionalInfo)
            throws JspException {
        return getSubmit(navigationEvent, style, title, title, additionalInfo);
    }

    protected String getSubmit(String navigationEvent, String style, String title, String value, String additionalInfo)
            throws JspException {
        if (navigationEvent != null) {
            String submitName = "operation" + Values.OPERATION_SEPARATOR + "wizardNavigationOnClick"
                    + Values.OPERATION_SEPARATOR + getName() + Values.OPERATION_SEPARATOR + navigationEvent;
            if (additionalInfo != null) {
                submitName += Values.OPERATION_SEPARATOR + additionalInfo;
            }

            String button = "<input type=\"submit\" name=\"" + submitName + "\" title=\"" + title + "\" value=\""
                    + value + "\" class=\"" + style + "\" " + "id=\"" + submitName + "submit\" />\n";
            return button;
        } else {
            return "";
        }
    }

    public static boolean showContent(Wizard wizard, WizardElement element) {
        if (element instanceof Step) {
            return showStepContent(wizard, (Step) element);
        } else {
            return showEndPageContent(wizard, (EndPage) element);
        }
    }

    private static boolean showStepContent(Wizard wizard, Step parsedStep) {
        if (wizard.getCurrentStep().isSummary() || parsedStep.isCurrent()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean showEndPageContent(Wizard wizard, EndPage parsedEndPage) {
        if (wizard.getCurrentElement() instanceof EndPage) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEditAction() {
        return editAction;
    }

    public void setEditAction(boolean editAction) {
        this.editAction = editAction;
    }

    public boolean isInsertAction() {
        return insertAction;
    }

    public void setInsertAction(boolean insertAction) {
        this.insertAction = insertAction;
    }
}
