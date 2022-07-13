package it.eng.spagoLite.actions.form;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.paging.IPaginator;
import it.eng.spagoLite.db.base.sorting.SortingRule;
import it.eng.spagoLite.form.Form;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.message.MessageBox.ViewMode;
import it.eng.spagoLite.security.IUser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ListAction<T extends Form, U extends IUser<?>> extends FormAction<T, U> {

    @Autowired
    private IPaginator paginator;
    public static final String NE_ELENCO = "elenco";
    public static final String NE_DETTAGLIO_VIEW = "dettaglioView";
    public static final String NE_DETTAGLIO_UPDATE = "update";
    public static final String NE_DETTAGLIO_INSERT = "dettaglioInsert";
    public static final String NE_DETTAGLIO_DELETE = "delete";
    public static final String NE_DETTAGLIO_SELECT = "select";
    public static final String NE_EXPORT_XLS = "esportaExcel";
    public static final String NE_FIRST = "first";
    public static final String NE_PREV_PAGE = "prevPage";
    public static final String NE_PREV = "prev";
    public static final String NE_NEXT = "next";
    public static final String NE_NEXT_PAGE = "nextPage";
    public static final String NE_SET_PAGE_SIZE = "setPageSize";
    public static final String NE_LAST = "last";
    public static final String NE_GOTO_ROW = "gotoRow";
    public static final String NE_DETTAGLIO_SAVE = "dettaglioSave";
    public static final String NE_DETTAGLIO_CANCEL = "dettaglioUndo";
    public static final String NE_UPDATED_ROW = "updatedRow";
    public static final String NE_SHOW_INACTIVE_ROWS = "showInactiveRows";
    public static final String NE_HIDE_INACTIVE_ROWS = "hideInactiveRows";
    /*
     * private String tableName; private String navigationEvent; private String riga; private String forceReload;
     * 
     * public String getTableName() { return tableName; }
     * 
     * public void setTableName(String tableName) { this.tableName = tableName; }
     * 
     * public String getNavigationEvent() { return navigationEvent; }
     * 
     * public void setNavigationEvent(String navigationEvent) { this.navigationEvent = navigationEvent; }
     * 
     * public String getRiga() { return riga; }
     * 
     * public void setRiga(String riga) { this.riga = riga; }
     * 
     * public String getForceReload() { return forceReload; }
     * 
     * public void setForceReload(String forceReload) { this.forceReload = forceReload; }
     */
    private static final long serialVersionUID = 4683122278656206133L;

    public abstract void loadDettaglio() throws EMFError;

    public abstract void undoDettaglio() throws EMFError;

    public abstract void insertDettaglio() throws EMFError;

    public abstract void update(List<?> list) throws EMFError;

    public abstract void delete(List<?> list) throws EMFError;

    public abstract void saveDettaglio() throws EMFError;

    public abstract void select(List<?> list) throws EMFError;

    public abstract void dettaglioOnClick() throws EMFError;

    public abstract void elencoOnClick() throws EMFError;

    public abstract void initOnClick() throws EMFError;

    public abstract void filterInactiveRecords(List<?> list) throws EMFError;

    /**
     * Metodo di utility che viene eseguito dopo ogni query di paginazione lazy
     */
    public abstract void postLazyLoad(List<?> list) throws EMFError;

    /**
     * Gestisce l'ordinamento sull'ordinamento delle liste
     *
     * @throws EMFError
     */
    public void listSortOnClick() throws EMFError {
        String tableName = getRequest().getParameter("table");
        String columnName = getRequest().getParameter("column");

        List<SingleValueField<Object>> list = (List<SingleValueField<Object>>) getForm().getComponent(tableName);

        if (!StringUtils.isEmpty(list.getComponent(columnName).getAlias())) {
            columnName = list.getComponent(columnName).getAlias();
        }

        SortingRule lastSortingRule = list.getTable().getLastSortingRule();
        list.getTable().clearSortingRule();
        if (lastSortingRule != null && lastSortingRule.getColumnName().equalsIgnoreCase(columnName)) {
            list.getTable().addSortingRule(columnName, -lastSortingRule.getSortType());
        } else {
            list.getTable().addSortingRule(columnName, SortingRule.ASC);
        }
        if (!this.lazySortCurrentPage(list)) {
            list.getTable().sort();
        }
        postLoad();

        forwardToPublisher(getLastPublisher());
    }

    /*
     * private void setParameters() { setTableName(getRequest().getParameter("table"));
     * setNavigationEvent(getRequest().getParameter("navigationEvent")); setRiga(getRequest().getParameter("riga"));
     * setForceReload(getRequest().getParameter("forceReload")); }
     * 
     * private void setParameters(String param[]) { setTableName(param[0]); setNavigationEvent(param[1]);
     * setRiga(param[2]); setForceReload(param[3]); }
     */
    public void listDetailNavigationOnClick() throws EMFError {
        setParameters();
        listDetailNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    public void listDetailNavigationOnClick(String param[]) throws EMFError {
        setParameters(param);
        listDetailNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    public void listNavigationOnClick() throws EMFError {
        setParameters();
        listNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    // probabilmente questa è una submit
    public void listNavigationOnClick(String param[]) throws EMFError {
        setParameters(param);
        listNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    /**
     * Gestisce gli eventi di navigazione sulle liste
     *
     * @param tableName
     * @param navigationEvent
     * @param riga
     * @param forceReload
     * 
     * @throws EMFError
     */
    protected void listNavigationOnClick(String tableName, String navigationEvent, String riga, String forceReload)
            throws EMFError {
        List<?> list = (List<?>) getForm().getComponent(tableName);
        // Forzo il goback se la tabella nella lista è null .. l'utente potrebbe aver cliccato il tasto back del browser
        if (list.getTable() == null) {
            getMessageBox().addWarning(
                    "Si è verificato un errore (probabilmente) a seguito dell'utilizzo del tasto Indietro del browser.\n E' stata recuperata l'ultima pagina aperta correttamente.");
            getMessageBox().setViewMode(ViewMode.plain);
            this.goBack(true);
            return;
        }
        calculateAuthorization(tableName);
        forwardToPublisher(getLastPublisher());
        if (navigationEvent.equalsIgnoreCase(NE_ELENCO)) {
            elencoOnClick();
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_VIEW)) {
            list.getTable().setCurrentRowIndex(new Integer(riga));
            if ("true".equalsIgnoreCase(forceReload)) {
                initOnClick();
            }
            loadDettaglio();
            if (isViewAction()) {
                dettaglioOnClick();
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_UPDATE)) {
            list.getTable().setCurrentRowIndex(new Integer(riga));
            loadDettaglio();
            dettaglioOnClick();
            // Check security
            list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            if (list.getRowSmandrupper().isEditable() && isEditAction()) {
                update(list);
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_INSERT)) {
            loadDettaglio();
            dettaglioOnClick();

            // Check security
            list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            if (list.getRowSmandrupper().isInsertable() && isInsertAction()) {
                insertDettaglio();
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_DELETE)) {
            list.getTable().setCurrentRowIndex(new Integer(riga));
            // loadDettaglio();
            dettaglioOnClick();

            // Check security
            list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            if (list.getRowSmandrupper().isDeletable() && isDeleteAction()) {
                delete(list);
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_FIRST)) {
            if (!this.lazyLoadFirstPage(list)) {
                list.getTable().first();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_PREV_PAGE)) {
            if (!this.lazyLoadPrevPage(list)) {
                list.getTable().prevPage();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_PREV)) {
            // evento invocato se sto paginando dal dettaglio
            // invoco il lazyloading solo se è una tabella paginata e se sono alla prima riga
            if (list.getTable().getLazyListBean() != null && list.getTable().getCurrentRowIndex() == 0) {
                this.lazyLoadPrevPage(list);
            } else {
                list.getTable().prev();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_NEXT)) {
            // evento invocato se sto paginando dal dettaglio
            // invoco il lazyloading solo se è una tabella paginata e se sono all'ultima riga (ie. l'ultima riga della
            // pagina +1 è maggiore del numero di risultati relativi)
            if (list.getTable().getLazyListBean() != null && list.getTable().getCurrentRowIndex()
                    + 1 > list.getTable().getLazyListBean().getMaxResult() - 1) {
                this.lazyLoadNextPage(list);
            } else {
                list.getTable().next();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_NEXT_PAGE)) {
            // evento invocato se sto paginando dalla tabella
            if (!this.lazyLoadNextPage(list)) {
                list.getTable().nextPage();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_GOTO_ROW)) {
            list.getTable().setCurrentRowIndex(new Integer(riga));
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_LAST)) {
            if (!this.lazyLoadLastPage(list)) {
                list.getTable().last();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_EXPORT_XLS)) {
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            String fileName = StringUtils.isNotBlank(list.getExcelFileName())
                    ? StringEscapeUtils.escapeJava(list.getExcelFileName())
                    : list.getName() + "_" + formatter.format(Calendar.getInstance().getTime());
            getResponse().setContentType("application/xls");
            getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xls");
            list.writeExcel(getServletOutputStream());
            freeze();
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_SAVE)) {
            saveDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_CANCEL)) {
            list.getTable().setCurrentRowIndex(new Integer(riga));
            undoDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_SELECT)) {
            list.getTable().setCurrentRowIndex(new Integer(riga));
            select(list);
        } else if (navigationEvent.equalsIgnoreCase(NE_SET_PAGE_SIZE)) {
            list.getTable().setPageSize(Integer.parseInt(getRiga()));
        } else if (navigationEvent.equalsIgnoreCase(NE_SHOW_INACTIVE_ROWS)) {
            list.setFilterValidRecords(false);
            filterInactiveRecords(list);
        } else if (navigationEvent.equalsIgnoreCase(NE_HIDE_INACTIVE_ROWS)) {
            list.setFilterValidRecords(true);
            filterInactiveRecords(list);
        }
        postLoad();
    }

    private boolean lazySortCurrentPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        // invoco l'ordinamento lazy se è una tabella paginata
        if (table.getLazyListBean() != null && (table.size() < table.getLazyListBean().getCountResultSize())) {
            table = getPaginator().sort(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    private boolean lazyLoadNextPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        // invoco la paginazione lazy se è una tabella paginata e se la pagina successiva non è contenuta interamente
        // nella lista che ho in memoria
        if (table.getLazyListBean() != null
                && table.getLastRowPageIndex() + table.getPageSize() + 1 > table.getLazyListBean().getMaxResult()) {
            table = getPaginator().nextPage(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    private boolean lazyLoadPrevPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        // invoco la paginazione lazy se è una tabella paginata e se la pagina successiva non è contenuta interamente
        // nella lista che ho in memoria
        if (table.getLazyListBean() != null && table.getFirstRowPageIndex() - table.getPageSize() < 0) {
            table = getPaginator().prevPage(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;
    }

    private boolean lazyLoadLastPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        if (table.getLazyListBean() != null && table.fullSize() > table.getLazyListBean().getFirstResult()
                + table.getLazyListBean().getMaxResult()) {
            table = getPaginator().lastPage(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    private boolean lazyLoadFirstPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        if (table.getLazyListBean() != null && table.getLazyListBean().getFirstResult() > 0) {
            table = getPaginator().firstPage(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    protected boolean lazyLoadGoPage(List<?> list, int page) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        // invoco la paginazione lazy se è una tabella paginata
        if (table.getLazyListBean() != null && // se il primo index della lista che ho in memoria è maggiore del primo
                                               // index della pagina di destinazione
                ((table.getLazyListBean().getFirstResult() > (page - 1) * table.getPageSize()) || // se l'ultimo index
                                                                                                  // della lista che ho
                                                                                                  // in memoria è minore
                                                                                                  // dell'ultimo index
                                                                                                  // della pagina di
                                                                                                  // destinazione
                        (table.getLazyListBean().getFirstResult()
                                + table.getLazyListBean().getMaxResult() < page * table.getPageSize() - 1))) {
            table = getPaginator().goPage(table, page);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    protected void listDetailNavigationOnClick(String tableName, String navigationEvent, String riga,
            String forceReload) throws EMFError {
        List<?> list = (List<?>) getForm().getComponent(tableName);
        forwardToPublisher(getLastPublisher());
        int newRiga = new Integer(riga);
        if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_UPDATE)) {
            if (list.postAndValidate(getRequest(), getMessageBox())) {
                updateEditableRows(list);
                list.getTable().setCurrentRowIndex(new Integer(riga)); // Cambio
                // riga
                update(list);
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_DELETE)) {
            // if (newRiga != list.getTable().getCurrentRowIndex()) {
            // if (list.postAndValidate(getRequest(), getMessageBox())) {
            // updateEditableRows(list);
            // list.getTable().setCurrentRowIndex(new Integer(riga));
            // }
            //
            // }
            list.getTable().setCurrentRowIndex(new Integer(riga));
            delete(list);

        }
    }

    private void updateEditableRows(List<?> list) throws EMFError {
        for (SingleValueField<?> field : list) {
            if (field.isEditMode()) {
                list.getTable().getCurrentRow().setObject(field.getName(), list.getColumn(field.getName()).parse());
                Object value = list.getTable().getCurrentRow().getObject(field.getName());
                list.getColumn(field.getName()).setValue((value != null) ? value.toString() : null);

            }
        }
    }

    @Override
    public void process() throws EMFError {
    }

    public IPaginator getPaginator() {
        return paginator;
    }

    public void setPaginator(IPaginator paginator) {
        this.paginator = paginator;
    }

}
