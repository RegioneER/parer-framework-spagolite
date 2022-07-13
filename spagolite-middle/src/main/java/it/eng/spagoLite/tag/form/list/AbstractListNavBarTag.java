package it.eng.spagoLite.tag.form.list;

import it.eng.spagoLite.actions.form.ListAction;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.form.base.BaseElements.Status;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.list.List;

import javax.servlet.jsp.JspException;

public class AbstractListNavBarTag extends AbstractListTag {

    private static final long serialVersionUID = 1L;

    private static final int[] pageSizes = { 10, 20, 50, 100 };

    protected void writeFirst() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null) {
            if (!modificainCorso() && table.getCurrentPageIndex() > 1) {

                writeln(getLink("navBarFirstPageLink", ListAction.NE_FIRST, "", null, "Vai alla prima pagina", -1,
                        false, true, getMainNavTable()));

            }
        }
    }

    // <span class="listLabel"> Risultati per pagina
    // </span> <select class="skipline"><option value="1">10</option></select>

    // <span class="">Risultati per pagina:</span>
    // <span> <a href="#">10</a>&nbsp;<a href="#">20</a>&nbsp;<a href="#">50</a>&nbsp;<a href="#">100</a></span></div>

    protected void writeRecordPerPage() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.size() > 0) {
            writeln("<span class=\"listLabel\"> Risultati per pagina: ");
            int pagesize = table.getPageSize();
            for (int ps : pageSizes) {
                if (ps != pagesize) {
                    // Uso il parametro riga per settare la dimensione della pagina
                    writeln(getLink("", ListAction.NE_SET_PAGE_SIZE, "", "" + ps, ps + " record per pagina", ps, false,
                            true, getMainNavTable()));
                } else {
                    writeln("<span>" + ps + "</span>");
                }
            }
            writeln("</span>");

        }
    }

    protected void writePrevPage() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null) {
            if (!modificainCorso() && table.getCurrentPageIndex() > 1) {

                writeln(getLink("navBarPrevPageLink", ListAction.NE_PREV_PAGE, "", null, "Vai alla pagina precedente",
                        -1, false, true, getMainNavTable()));

            }
        }
    }

    protected void writePrev() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        boolean enabled = table.getCurrentRowIndex() > 0;
        enabled = (table.getLazyListBean() != null) ? (enabled || table.getLazyListBean().getFirstResult() > 0)
                : enabled;
        if (table != null) {
            if (!modificainCorso() && enabled) {
                // writeLink(ListAction.NE_PREV, PREV);
                writeln(getLink("navBarPrevLink", ListAction.NE_PREV, "", null, "Vai al record precedente", -1, false,
                        true, getMainNavTable()));

            } else {
                // writeLink(null, PREV_DISABLED);
                writeln(getLink("navBarPrevDisabledLink", null, null, null, null, -1, false, true, getMainNavTable()));

            }
        }
    }

    protected void writeElenco() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null) {
            if (!modificainCorso()) {
                // writeLink(ListAction.NE_ELENCO, ELENCO);
                // writeLink(ListAction.NE_ELENCO, BACK_URL_IMG, "Indietro");
                writeln(getLink("navBarBackLink", ListAction.NE_ELENCO, "", "Indietro", "Indietro", -1, false, true,
                        getMainNavTable()));
            } else {
                // writeLink(null, BACK_DISABLED, "");
                writeln(getLink("navBarBackDisabledLink", null, null, null, null, -1, false, true, getMainNavTable()));

            }
        }
    }

    protected void writeBack() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null) {
            if (!modificainCorso()) {
                writeLink(ListAction.NE_ELENCO, BACK_URL_IMG);
                writeln("&nbsp;Indietro");
            } else {
                writeLink(null, BACK_DISABLED);
                writeln("&nbsp;Indietro");
            }
        }
    }

    protected void writeNext() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        boolean enabled = (table.getLazyListBean() != null)
                ? table.getCurrentRowIndex() + table.getLazyListBean().getFirstResult() < table.fullSize() - 1
                : table.getCurrentRowIndex() < table.size() - 1;
        if (table != null) {
            if (!modificainCorso() && enabled) {
                // writeLink(ListAction.NE_NEXT, NEXT);
                writeln(getLink("navBarNextLink", ListAction.NE_NEXT, "", null, "Vai al record successivo", -1, false,
                        true, getMainNavTable()));

            } else {
                // writeLink(null, NEXT_DISABLED);
                writeln(getLink("navBarNextDisabledLink", null, null, null, null, -1, false, true, getMainNavTable()));

            }
        }
    }

    protected void writeNextPage() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null) {
            if (!modificainCorso() && table.getPageSize() > 0 && table.getCurrentPageIndex() < table.getPages()) {
                writeln(getLink("navBarNextPageLink", ListAction.NE_NEXT_PAGE, "", null, "Vai alla pagina successiva",
                        -1, false, true, getMainNavTable()));
            }
        }
    }

    protected void writeLast() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null) {
            if (!modificainCorso() && table.getPageSize() > 0 && table.getCurrentPageIndex() < table.getPages()) {

                writeln(getLink("navBarLastPageLink", ListAction.NE_LAST, "", null, "Vai all'ultima pagina", -1, false,
                        true, getMainNavTable()));

            } else {

                // writeln(getLink("navBarLastPageDisabledLink", null, null, null, null, -1,
                // false,true,getMainNavTable()));

            }
        }
    }

    protected void writeRecordCounter() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.size() > 0) {
            if (table.getLazyListBean() != null) {
                writeln("<span class=\"listLabel\">Record "
                        + (table.getCurrentRowIndex() + table.getLazyListBean().getFirstResult() + 1) + " di "
                        + table.fullSize() + "</span>");
            } else {
                writeln("<span class=\"listLabel\">Record " + (table.getCurrentRowIndex() + 1) + " di "
                        + table.fullSize() + "</span>");
            }
        }
    }

    protected void writeRecordNumber() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.size() > 0) {

            writeln("<span class=\"listLabel\">" + table.fullSize() + " risultati</span>");
        }
    }

    protected void writeIsListSortable() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.size() > 0) {

            if (!isListInMemory() && !isLazySortEnabled()) {
                writeln("<span class=\"listLabel\">Lista non ordinabile (superati i "
                        + getComponent().getTable().getLazyListBean().getMaxResult() + " record)</span>");
            } else if (getComponent().getTable().getLazyListBean() == null) {
                // writeln("Lista non ordinabile");
            }
        }
    }

    protected void writePageCounter() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.getPageSize() > 0 && table.size() > 0) {
            writeln("<span class=\"listLabel\">Pagina " + (table.getCurrentPageIndex()) + " di " + table.getPages()
                    + "</span>");
        }
    }

    protected void writeExcel() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.getPageSize() > 0 && table.size() > 0) {
            // writeln("<span class=\"floatRight\">" + getLink(ListAction.NE_EXPORT_XLS, EXPORT_XLS, "") + "</span>");
            // writeln("<span class=\"floatRight\">" + getLink("navBarExportExcelLink", ListAction.NE_EXPORT_XLS, "",
            // "Excel", "Esporta la lista in excel", -1, false,true,getMainNavTable()) + "</span>");
            writeln("<span class=\"floatRight\">"
                    + getLink("", ListAction.NE_EXPORT_XLS, EXCEL_IMG, null, null, -1, false, true, getMainNavTable())
                    + "</span>");
        }
    }

    protected void writeHideInactiveRecords() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null) {
            writeln("<span class=\"activeRecords\">");
            writeln(getLink("", ListAction.NE_HIDE_INACTIVE_ROWS, "", "Nascondi record non attivi",
                    "Nascondi record non attivi", -1, false, true, getMainNavTable()));
            writeln("</span>");
        }
    }

    protected void writeShowInactiveRecords() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null) {
            writeln("<span class=\"activeRecords\">");
            writeln(getLink("", ListAction.NE_SHOW_INACTIVE_ROWS, "", "Mostra record non attivi",
                    "Mostra record non attivi", -1, false, true, getMainNavTable()));
            writeln("</span>");
        }
    }

    /*
     * i metodi qui sotto non dipendono solo dal fatto che il componente sia o meno modificabile ma anche dai permessi
     * associati all'utente
     */

    // Elenco
    protected void writeInsert() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.getPageSize() > 0) {
            writeln("<span class=\"floatRight\">" + getLink("", ListAction.NE_DETTAGLIO_INSERT, INSERT_IMG, null, null,
                    -1, false, true, getMainNavTable()) + "</span>");
        }
    }

    protected void writeUpdate() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.getPageSize() > 0 && table.size() > 0 && isRowEditable(table.getCurrentRow())) {
            writeln("<span class=\"floatRight\">" + getLink("", ListAction.NE_DETTAGLIO_UPDATE, UPDATE_IMG, null, null,
                    table.getCurrentPageIndex(), false, true, getMainNavTable()) + "</span>");
        }
    }

    protected void writeDelete() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.getPageSize() > 0 && table.size() > 0 && isRowDeletable(table.getCurrentRow())) {
            writeln("<span class=\"floatRight\">" + getLink("", ListAction.NE_DETTAGLIO_DELETE, DELETE_IMG, null, null,
                    table.getCurrentPageIndex(), false, true, getMainNavTable()) + "</span>");
        }
    }

    protected void writeDetailUpdate() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.size() > 0 && isRowEditable(table.getCurrentRow()) && isComponentEditable()
                && !modificainCorso()) {
            writeln("<span class=\"floatRight\">" + getSubmit(ListAction.NE_DETTAGLIO_UPDATE, UPDATE_STYLE, "Modifica",
                    "Modifica", table.getCurrentRowIndex(), false) + "</span>");
        }
    }

    protected void writeDetailInsert() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.size() > 0 && isRowInsertable(table.getCurrentRow()) && isComponentEditable()
                && !modificainCorso()) {
            writeln("<span class=\"floatRight\">" + getSubmit(ListAction.NE_DETTAGLIO_INSERT, INSERT_STYLE, "Inserisci",
                    "Inserisci", table.getCurrentRowIndex(), false) + "</span>");
        }
    }

    protected void writeDetailDelete() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.size() > 0 && isRowDeletable(table.getCurrentRow()) && isComponentEditable()
                && !modificainCorso()) {
            writeln("<span class=\"floatRight\">" + getSubmit(ListAction.NE_DETTAGLIO_DELETE, DELETE_STYLE, "Elimina",
                    "Elimina", table.getCurrentRowIndex(), false) + "</span>");
        }
    }

    protected void writeDetailSave() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.getPageSize() > 0 && table.size() > 0 && modificainCorso()) {
            writeln("<span class=\"floatRight\">" + getSubmit(ListAction.NE_DETTAGLIO_SAVE, SAVE_STYLE, "Salva",
                    "Salva", table.getCurrentRowIndex(), false) + "</span>");
        }
    }

    protected void writeDetailCancel() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();
        if (table != null && table.getPageSize() > 0 && table.size() > 0 && modificainCorso()) {
            writeln("<span class=\"floatRight\">" + getSubmit(ListAction.NE_DETTAGLIO_CANCEL, CANCEL_STYLE, "Annulla",
                    "Annulla", table.getCurrentRowIndex(), false) + "</span>");
        }
    }

    protected void writeSeparator() throws JspException {
        write("<span class=\"floatRight\"> | </span>");
    }

    private boolean modificainCorso() {
        List<SingleValueField<?>> list = getComponent();
        if (Status.insert.equals(list.getStatus()) || Status.delete.equals(list.getStatus())
                || Status.update.equals(list.getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    // table != null
    private boolean isComponentEditable() {
        List<SingleValueField<?>> list = getComponent();

        if (Status.delete.equals(list.getStatus()) || Status.update.equals(list.getStatus())) {
            return false;
        }

        if (getComponent().getTable() == null) {
            return false;
        }

        if (getComponent().getTable().size() <= 0) {
            return false;
        }

        return true;
    }

    @Override
    public int doStartTag() throws JspException {
        BaseTableInterface<?> table = getComponent().getTable();

        if (table != null) {
            writeln("<div class=\"listToolBar\">");
            writeFirst();
            writePrevPage();
            writePrev();
            writeNext();
            writeNextPage();
            writeLast();
            writePageCounter();

            writeln("</div>");
        }

        return SKIP_BODY;
    }

}