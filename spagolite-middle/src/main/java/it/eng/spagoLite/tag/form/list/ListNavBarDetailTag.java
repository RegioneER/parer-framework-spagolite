package it.eng.spagoLite.tag.form.list;

import it.eng.spagoLite.actions.form.ListAction;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.form.list.List;

import javax.servlet.jsp.JspException;

public class ListNavBarDetailTag extends AbstractListNavBarTag {

    private static final long serialVersionUID = 1L;

    private boolean hideOperationButton;

    private String insertAction = "toolbar/insert";
    private String editAction = "toolbar/edit";
    private String deleteAction = "toolbar/delete";

    @Override
    public int doStartTag() throws JspException {
        if (getName() != null) {

            BaseTableInterface<?> table = getComponent().getTable();

            if (table != null) {
                if (table.size() == 0) {
                    return SKIP_BODY;
                }
                writeln("<div class=\"listToolBar\">");
                calculateAuthorization();

                // Pulsanti di modifica/salvataggio
                if (!getComponent().isReadOnly() && !isHideOperationButton()) {
                    if ((isEditAction() && !getComponent().isHideUpdateButton())
                            || ((isInsertAction()) && (!((List) getComponent()).isHideInsertButton()))) {
                        debugAuthorization(insertAction);
                        debugAuthorization(editAction);
                        writeDetailSave();
                    }
                    if (isEditAction() && !getComponent().isHideUpdateButton()) {
                        debugAuthorization(editAction);
                        writeDetailUpdate();
                        writeDetailCancel();
                    }
                    if (isDeleteAction() && !getComponent().isHideDeleteButton()) {
                        debugAuthorization(deleteAction);
                        writeDetailDelete();
                    }
                }

                // Pulsanti di navigazione
                writeElenco();
                writePrev();
                writeRecordCounter();
                writeNext();

            }
        } else {
            writeln("<div class=\"listToolBar\">");
            writeln(getLink("navBarBackLink", ListAction.NE_ELENCO, "", "Indietro", "Indietro", -1, false, true));
        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        writeln("</div>");
        return super.doEndTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        // TODO Auto-generated method stub
        return super.doAfterBody();
    }

    public boolean isHideOperationButton() {
        return hideOperationButton;
    }

    public void setHideOperationButton(boolean hideOperationButton) {
        this.hideOperationButton = hideOperationButton;
    }

    private void calculateAuthorization() {

        setEditAction(isUserAuthorized(editAction));
        setDeleteAction(isUserAuthorized(deleteAction));
        setInsertAction(isUserAuthorized(insertAction));
    }

}
